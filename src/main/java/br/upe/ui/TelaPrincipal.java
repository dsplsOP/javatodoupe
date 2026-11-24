package br.upe.ui;

import br.upe.controller.TarefaControlador;
import br.upe.model.Prioridade;
import br.upe.model.Tarefa;
import br.upe.model.TarefaTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TelaPrincipal {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JPanel pnlMain;
    private JTextField txtDescricaoTarefa;
    private JComboBox<String> cmbCategoria;
    private JComboBox<Prioridade> cmbPrioridade;
    private JComboBox<String> cmbFiltroCategoria;
    private JButton btnAdicionarTarefa;
    private JButton btnPrazo;
    private JButton btnExcluirTarefa;
    private JPanel pnlAdicionar;
    private JTable tblTarefas;
    private JCheckBox chkExibirFinalizadas;

    private TarefaControlador controlador;
    private LocalDate prazoSelecionado; // prazo escolhido para a próxima tarefa (pode ser null)

    public TelaPrincipal() {
        super();

        String[] categorias = {"Saúde", "Trabalho", "Estudo", "Casa",
                "Pessoal", "Família", "Financeiro", "Outros"};

        cmbFiltroCategoria.addItem("Todas");
        for (String c : categorias) {
            cmbCategoria.addItem(c);
            cmbFiltroCategoria.addItem(c);
        }

        cmbPrioridade.setModel(new DefaultComboBoxModel<>(Prioridade.values()));
        atualizarTextoBotaoPrazo();

        btnPrazo.addActionListener(e -> {
            CalendarioDialog dialogo = new CalendarioDialog(
                    SwingUtilities.getWindowAncestor(pnlMain), prazoSelecionado);
            dialogo.setVisible(true);
            if (dialogo.isConfirmado()) {
                prazoSelecionado = dialogo.getDataSelecionada();
                atualizarTextoBotaoPrazo();
            }
        });

        btnAdicionarTarefa.addActionListener(e -> {
            String categoria = (String) cmbCategoria.getSelectedItem();
            Prioridade prioridade = (Prioridade) cmbPrioridade.getSelectedItem();
            adicionarTarefa(txtDescricaoTarefa.getText(), categoria, prioridade, prazoSelecionado);
            txtDescricaoTarefa.setText("");
            prazoSelecionado = null;
            atualizarTextoBotaoPrazo();
        });

        chkExibirFinalizadas.addActionListener(e -> {
            boolean selecionado = ((JCheckBox) e.getSource()).isSelected();
            controlador.exibirFinalizadas(selecionado);
        });

        cmbFiltroCategoria.addActionListener(e ->
                controlador.filtrarPorCategoria((String) cmbFiltroCategoria.getSelectedItem())
        );

        btnExcluirTarefa.addActionListener(e -> excluirTarefasSelecionadas());
    }

    private void atualizarTextoBotaoPrazo() {
        btnPrazo.setText(prazoSelecionado == null
                ? "Prazo..."
                : "Prazo: " + prazoSelecionado.format(FORMATO_DATA));
    }

    private void excluirTarefasSelecionadas() {
        // Se houver uma célula em edição, confirma a edição antes de excluir
        if (tblTarefas.isEditing()) {
            tblTarefas.getCellEditor().stopCellEditing();
        }

        int[] linhasTela = tblTarefas.getSelectedRows();
        if (linhasTela.length == 0) {
            JOptionPane.showMessageDialog(pnlMain,
                    "Selecione na tabela a tarefa que deseja excluir.",
                    "Excluir tarefa", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String mensagem = (linhasTela.length == 1)
                ? "Deseja excluir a tarefa selecionada?"
                : "Deseja excluir as " + linhasTela.length + " tarefas selecionadas?";
        int resposta = JOptionPane.showConfirmDialog(pnlMain, mensagem,
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        // A tabela pode estar ordenada pelo cabeçalho: converte linha da tela -> linha do modelo
        int[] linhasModelo = new int[linhasTela.length];
        for (int i = 0; i < linhasTela.length; i++) {
            linhasModelo[i] = tblTarefas.convertRowIndexToModel(linhasTela[i]);
        }
        controlador.excluirTarefas(linhasModelo);
    }

    private void adicionarTarefa(String texto, String categoria, Prioridade prioridade, LocalDate prazo) {
        int ordem = controlador.getTarefaTableModel().getTotalTarefas();
        Tarefa tarefa = new Tarefa(texto, categoria, ordem, prioridade);
        tarefa.setDataVencimento(prazo);
        controlador.adicionarTarefa(tarefa);
    }

    public JPanel getPnlMain() {
        return this.pnlMain;
    }

    public TarefaControlador getControlador() {
        return controlador;
    }

    private void createUIComponents() {
        controlador = new TarefaControlador();
        tblTarefas = new JTable(controlador.getTarefaTableModel());
        tblTarefas.setAutoCreateRowSorter(true); // clique no cabeçalho para ordenar
        tblTarefas.getColumnModel().getColumn(0).setMaxWidth(80);

        // Mostra datas como dd/MM/yyyy
        tblTarefas.setDefaultRenderer(LocalDate.class, new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                setText(value == null ? "" : ((LocalDate) value).format(FORMATO_DATA));
            }
        });
        tblTarefas.setToolTipText("Dê dois cliques na coluna Vencimento para alterar o prazo");

        // Duplo clique na coluna Vencimento abre o calendário
        tblTarefas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) return;
                int linhaTela = tblTarefas.rowAtPoint(e.getPoint());
                int colunaTela = tblTarefas.columnAtPoint(e.getPoint());
                if (linhaTela < 0 || colunaTela < 0) return;
                if (tblTarefas.convertColumnIndexToModel(colunaTela) != 4) return;

                int linha = tblTarefas.convertRowIndexToModel(linhaTela);
                TarefaTableModel modelo = controlador.getTarefaTableModel();
                LocalDate atual = (LocalDate) modelo.getValueAt(linha, 4);

                CalendarioDialog dialogo = new CalendarioDialog(
                        SwingUtilities.getWindowAncestor(tblTarefas), atual);
                dialogo.setVisible(true);
                if (dialogo.isConfirmado()) {
                    modelo.setValueAt(dialogo.getDataSelecionada(), linha, 4);
                }
            }
        });
    }

}
