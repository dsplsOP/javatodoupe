package br.upe.controller;

import br.upe.model.Tarefa;
import br.upe.model.TarefaTableModel;

import java.util.Comparator;

public class TarefaControlador {

    // Atributos
    private TarefaTableModel tarefaTableModel;

    // Construtor
    public TarefaControlador() {
        tarefaTableModel = new TarefaTableModel();
    }

    // Métodos de negócio
    public void adicionarTarefaAtiva(Tarefa tarefa) {
        this.tarefaTableModel.getTarefasAtivas().add(tarefa);
        this.tarefaTableModel.fireTableDataChanged();
    }

    public void exibirFinalizadas(boolean exibir) {
        tarefaTableModel.setExibirFinalizadas(exibir);
    }

    public void ordenarPorPrioridade(boolean maisUrgentePrimeiro) {
        Comparator<Tarefa> comparador = Comparator.comparing(Tarefa::getPrioridade);
        if (maisUrgentePrimeiro) {
            comparador = comparador.reversed();
        }
        this.tarefaTableModel.getTarefasAtivas().sort(comparador);
        this.tarefaTableModel.fireTableDataChanged();
    }

    // Getters e Setters
    public TarefaTableModel getTarefaTableModel() {
        return tarefaTableModel;
    }

    public void setTarefaTableModel(TarefaTableModel tarefaTableModel) {
        this.tarefaTableModel = tarefaTableModel;
    }

}