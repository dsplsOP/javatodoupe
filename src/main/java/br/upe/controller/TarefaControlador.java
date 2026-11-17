package br.upe.controller;

import br.upe.model.Tarefa;
import br.upe.model.TarefaTableModel;

public class TarefaControlador {

    // Atributos
    private TarefaTableModel tarefaTableModel;

    // Construtor
    public TarefaControlador() {
        tarefaTableModel = new TarefaTableModel();
    }

    // Métodos de negócio
    public void adicionarTarefa(Tarefa tarefa) {
        this.tarefaTableModel.adicionarTarefa(tarefa);
    }

    public void exibirFinalizadas(boolean exibir) {
        tarefaTableModel.setExibirFinalizadas(exibir);
    }

    public void filtrarPorCategoria(String categoria) {
        tarefaTableModel.setFiltroCategoria(categoria);
    }

    public void ordenarPorPrioridade(boolean maisUrgentePrimeiro) {
        tarefaTableModel.ordenarPorPrioridade(maisUrgentePrimeiro);
    }

    // Getters e Setters
    public TarefaTableModel getTarefaTableModel() {
        return tarefaTableModel;
    }

    public void setTarefaTableModel(TarefaTableModel tarefaTableModel) {
        this.tarefaTableModel = tarefaTableModel;
    }
}