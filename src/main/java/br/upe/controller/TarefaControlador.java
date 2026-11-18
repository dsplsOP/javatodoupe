package br.upe.controller;

import br.upe.model.Tarefa;
import br.upe.model.TarefaTableModel;
import br.upe.persistence.TarefaRepositorio;

import javax.swing.event.TableModelEvent;

public class TarefaControlador {

    // Atributos
    private TarefaTableModel tarefaTableModel;
    private final TarefaRepositorio repositorio;

    // Construtor
    public TarefaControlador() {
        tarefaTableModel = new TarefaTableModel();
        repositorio = new TarefaRepositorio();

        // 1) Carrega as tarefas salvas na última execução
        tarefaTableModel.setTarefas(repositorio.carregar());

        // 2) A partir daqui, qualquer mudança nos dados é salva automaticamente
        ativarSalvamentoAutomatico();
    }

    private void ativarSalvamentoAutomatico() {
        tarefaTableModel.addTableModelListener((TableModelEvent e) -> {
            TarefaTableModel modelo = (TarefaTableModel) e.getSource();
            repositorio.salvar(modelo.getTodasTarefas());
        });
    }

    // Métodos de negócio
    public void adicionarTarefa(Tarefa tarefa) {
        this.tarefaTableModel.adicionarTarefa(tarefa);
    }

    public void excluirTarefas(int[] linhasModelo) {
        tarefaTableModel.removerTarefas(linhasModelo);
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
        ativarSalvamentoAutomatico();
    }
}