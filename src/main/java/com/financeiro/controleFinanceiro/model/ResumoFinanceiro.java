package com.financeiro.controleFinanceiro.model;

public class ResumoFinanceiro {

    private Double saldo;
    private Double receitas;
    private Double despesas;

    public ResumoFinanceiro(Double despesas, Double receitas, Double saldo) {
        this.despesas = despesas;
        this.receitas = receitas;
        this.saldo = saldo;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getReceitas() {
        return receitas;
    }

    public void setReceitas(Double receitas) {
        this.receitas = receitas;
    }

    public Double getDespesas() {
        return despesas;
    }

    public void setDespesas(Double despesas) {
        this.despesas = despesas;
    }
}
