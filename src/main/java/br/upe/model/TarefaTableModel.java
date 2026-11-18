package br.upe.model;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TarefaTableModel extends AbstractTableModel {

    // Lista única com TODAS as tarefas (ativas e finalizadas).
    // O que aparece na tabela é calculado a partir dela pelos filtros abaixo.
    private final List<Tarefa> tarefas;

    private boolean exibirFinalizadas;
    private String filtroCategoria = "Todas";

    public TarefaTableModel() {
        tarefas = new ArrayList<>();
    }

    // Retorna apenas as tarefas que devem aparecer na tabela agora
    private List<Tarefa> getTarefasVisiveis() {
        List<Tarefa> visiveis = new ArrayList<>();
        for (Tarefa t : tarefas) {
            boolean finalizada = Boolean.TRUE.equals(t.isFinalizada());

            // Finalizada só aparece se "Exibir finalizadas" estiver marcado
            if (finalizada && !exibirFinalizadas) {
                continue;
            }
            // Filtro por categoria
            if (!"Todas".equals(filtroCategoria) && !filtroCategoria.equals(t.getCategoria())) {
                continue;
            }
            visiveis.add(t);
        }
        return visiveis;
    }

    @Override
    public int getRowCount() {
        return getTarefasVisiveis().size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Concluída";
            case 1: return "Descrição";
            case 2: return "Categoria";
            case 3: return "Prioridade";
            default: return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tarefa tarefa = getTarefasVisiveis().get(rowIndex);
        switch (columnIndex) {
            case 0: return tarefa.isFinalizada();
            case 1: return tarefa.getDescricao();
            case 2: return tarefa.getCategoria();
            case 3: return tarefa.getPrioridade();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int c) {
        switch (c) {
            case 0: return Boolean.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return Prioridade.class;
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex != 0) {
            return;
        }
        Tarefa tarefa = getTarefasVisiveis().get(rowIndex);
        boolean finalizada = (Boolean) aValue;
        tarefa.setFinalizada(finalizada);
        tarefa.setDataFinalizacao(finalizada ? LocalDate.now() : null);

        // Recalcula a tabela: se "Exibir finalizadas" estiver desmarcado,
        // a tarefa recém-finalizada some da lista automaticamente.
        this.fireTableDataChanged();
    }

    // ---------- Operações usadas pelo controlador ----------

    public void adicionarTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
        this.fireTableDataChanged();
    }

    public int getTotalTarefas() {
        return tarefas.size();
    }

    // Substitui todas as tarefas (usado ao carregar o arquivo salvo)
    public void setTarefas(List<Tarefa> novasTarefas) {
        tarefas.clear();
        tarefas.addAll(novasTarefas);
        renumerarOrdem();
        this.fireTableDataChanged();
    }

    // Remove as tarefas das linhas informadas.
    // As linhas são índices do MODELO (não da tela), pois a tabela pode estar
    // ordenada ou filtrada.
    public void removerTarefas(int[] linhasModelo) {
        List<Tarefa> visiveis = getTarefasVisiveis();
        List<Tarefa> remover = new ArrayList<>();
        for (int linha : linhasModelo) {
            if (linha >= 0 && linha < visiveis.size()) {
                remover.add(visiveis.get(linha));
            }
        }
        tarefas.removeAll(remover);
        renumerarOrdem();
        this.fireTableDataChanged();
    }

    // Mantém o campo "ordem" coerente (0, 1, 2...) depois de carregar/excluir
    private void renumerarOrdem() {
        for (int i = 0; i < tarefas.size(); i++) {
            tarefas.get(i).setOrdem(i);
        }
    }

    public void setExibirFinalizadas(boolean selecionado) {
        this.exibirFinalizadas = selecionado;
        this.fireTableDataChanged();
    }

    public boolean isExibirFinalizadas() {
        return exibirFinalizadas;
    }

    public void setFiltroCategoria(String categoria) {
        this.filtroCategoria = (categoria == null) ? "Todas" : categoria;
        this.fireTableDataChanged();
    }

    // Ordena a lista inteira; os filtros continuam valendo por cima
    public void ordenarPorPrioridade(boolean maisUrgentePrimeiro) {
        Comparator<Tarefa> comparador = Comparator.comparing(Tarefa::getPrioridade);
        if (maisUrgentePrimeiro) {
            comparador = comparador.reversed();
        }
        tarefas.sort(comparador);
        this.fireTableDataChanged();
    }

    public List<Tarefa> getTodasTarefas() {
        return tarefas;
    }
}