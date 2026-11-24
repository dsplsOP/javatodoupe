package br.upe.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Estatisticas {

    private final List<Tarefa> tarefas;
    private final LocalDate hoje;

    public Estatisticas(List<Tarefa> tarefas) {
        this(tarefas, LocalDate.now());
    }

    public Estatisticas(List<Tarefa> tarefas, LocalDate hoje) {
        this.tarefas = new ArrayList<>(tarefas);
        this.hoje = hoje;
    }

    public int getTotal() {
        return tarefas.size();
    }

    public int getConcluidas() {
        int cont = 0;
        for (Tarefa t : tarefas) {
            if (estaConcluida(t)) {
                cont++;
            }
        }
        return cont;
    }

    public int getPendentes() {
        return getTotal() - getConcluidas();
    }

    public double getPercentualConclusao() {
        if (getTotal() == 0) {
            return 0.0;
        }
        return (getConcluidas() * 100.0) / getTotal();
    }

    public int getAtrasadas() {
        int cont = 0;
        for (Tarefa t : tarefas) {
            if (!estaConcluida(t) && t.getDataVencimento() != null
                    && t.getDataVencimento().isBefore(hoje)) {
                cont++;
            }
        }
        return cont;
    }

    public int getConcluidasNoPrazo() {
        int cont = 0;
        for (Tarefa t : tarefas) {
            if (temPrazoEFinalizacao(t) && !t.getDataFinalizacao().isAfter(t.getDataVencimento())) {
                cont++;
            }
        }
        return cont;
    }

    public int getConcluidasForaDoPrazo() {
        int cont = 0;
        for (Tarefa t : tarefas) {
            if (temPrazoEFinalizacao(t) && t.getDataFinalizacao().isAfter(t.getDataVencimento())) {
                cont++;
            }
        }
        return cont;
    }

    public Map<Prioridade, Integer> getDistribuicaoPorPrioridade() {
        Map<Prioridade, Integer> mapa = new EnumMap<>(Prioridade.class);
        for (Prioridade p : Prioridade.values()) {
            mapa.put(p, 0);
        }
        for (Tarefa t : tarefas) {
            if (t.getPrioridade() != null) {
                mapa.merge(t.getPrioridade(), 1, Integer::sum);
            }
        }
        return mapa;
    }

    private boolean estaConcluida(Tarefa t) {
        return Boolean.TRUE.equals(t.isFinalizada());
    }

    private boolean temPrazoEFinalizacao(Tarefa t) {
        return estaConcluida(t) && t.getDataVencimento() != null && t.getDataFinalizacao() != null;
    }
}