package br.upe.ui;

import br.upe.controller.TarefaControlador;
import br.upe.model.Prioridade;
import br.upe.model.Tarefa;

import javax.swing.*;

public class TelaPrincipal {
    private JPanel pnlMain;
    private JTextField txtDescricaoTarefa;
    private JButton btnAdicionarTarefa;
    private JPanel pnlAdicionar;
    private JTable tblTarefas;
    private JCheckBox chkExibirFinalizadas;
    private JComboBox<Prioridade> cmbPrioridade;
    private JButton btnOrdenarPrioridade;

    private TarefaControlador controlador;
    private boolean maisUrgentePrimeiro = true;

    public TelaPrincipal() {
        super();

        cmbPrioridade.setModel(new DefaultComboBoxModel<>(Prioridade.values()));

        btnAdicionarTarefa.addActionListener(e -> {
            adicionarTarefa(txtDescricaoTarefa.getText());
            txtDescricaoTarefa.setText("");
        });

        chkExibirFinalizadas.addActionListener(e -> {
            boolean selecionado = ((JCheckBox) e.getSource()).isSelected();
            controlador.exibirFinalizadas(selecionado);
        });

        btnOrdenarPrioridade.addActionListener(e -> {
            controlador.ordenarPorPrioridade(maisUrgentePrimeiro);
            maisUrgentePrimeiro = !maisUrgentePrimeiro; // alterna a cada clique
        });
    }

    private void adicionarTarefa(String texto) {
        Prioridade prioridade = (Prioridade) cmbPrioridade.getSelectedItem();
        int ordem = controlador.getTarefaTableModel().getTarefasAtivas().size();
        Tarefa tarefa = new Tarefa(texto, ordem, prioridade);
        controlador.adicionarTarefaAtiva(tarefa);
    }

    public JPanel getPnlMain() {
        return this.pnlMain;
    }

    private void createUIComponents() {
        controlador = new TarefaControlador();
        tblTarefas = new JTable(controlador.getTarefaTableModel());
        tblTarefas.getColumnModel().getColumn(0).setMaxWidth(20);
    }

}