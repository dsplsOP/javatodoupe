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