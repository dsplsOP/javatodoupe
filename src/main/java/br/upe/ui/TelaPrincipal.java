package br.upe.ui;

import br.upe.controller.TarefaControlador;
import br.upe.model.Prioridade;
import br.upe.model.Tarefa;

import javax.swing.*;

public class TelaPrincipal {
    private JPanel pnlMain;
    private JTextField txtDescricaoTarefa;
    private JComboBox<String> cmbCategoria;
    private JComboBox<Prioridade> cmbPrioridade;
    private JComboBox<String> cmbFiltroCategoria;
    private JButton btnAdicionarTarefa;
    private JButton btnExcluirTarefa;
    private JPanel pnlAdicionar;
    private JTable tblTarefas;
    private JCheckBox chkExibirFinalizadas;

    private TarefaControlador controlador;

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

        btnAdicionarTarefa.addActionListener(e -> {
            String categoria = (String) cmbCategoria.getSelectedItem();
            Prioridade prioridade = (Prioridade) cmbPrioridade.getSelectedItem();
            adicionarTarefa(txtDescricaoTarefa.getText(), categoria, prioridade);
            txtDescricaoTarefa.setText("");
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

    private void adicionarTarefa(String texto, String categoria, Prioridade prioridade) {
        int ordem = controlador.getTarefaTableModel().getTotalTarefas();
        Tarefa tarefa = new Tarefa(texto, categoria, ordem, prioridade);
        controlador.adicionarTarefa(tarefa);
    }

    public JPanel getPnlMain() {
        return this.pnlMain;
    }

    private void createUIComponents() {
        controlador = new TarefaControlador();
        tblTarefas = new JTable(controlador.getTarefaTableModel());
        tblTarefas.setAutoCreateRowSorter(true); // clique no cabeçalho para ordenar
        tblTarefas.getColumnModel().getColumn(0).setMaxWidth(80);
    }

}