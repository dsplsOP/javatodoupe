package br.upe.model;

import java.time.LocalDate;

public class Tarefa {

    private String descricao;
    private String categoria;
    private LocalDate dataCriacao;
    private LocalDate dataFinalizacao;
    private LocalDate dataVencimento;
    private Boolean finalizada;
    private int ordem;
    private Prioridade prioridade;

    public Tarefa(String texto, String categoria, int ordem, Prioridade prioridade) {
        this.dataCriacao = LocalDate.now();
        this.descricao = texto;
        this.categoria = categoria;
        this.ordem = ordem;
        this.prioridade = prioridade;
        this.finalizada = false;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(LocalDate dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Boolean finalizada) {
        this.finalizada = finalizada;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
}