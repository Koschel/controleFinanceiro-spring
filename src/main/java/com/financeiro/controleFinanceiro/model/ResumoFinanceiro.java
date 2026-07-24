package com.financeiro.controleFinanceiro.model;

import java.math.BigDecimal;

public class ResumoFinanceiro {

    private BigDecimal saldo;
    private BigDecimal receitas;
    private BigDecimal despesas;

    public ResumoFinanceiro(BigDecimal despesas, BigDecimal receitas, BigDecimal saldo) {
        this.despesas = despesas;
        this.receitas = receitas;
        this.saldo = saldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getReceitas() {
        return receitas;
    }

    public void setReceitas(BigDecimal receitas) {
        this.receitas = receitas;
    }

    public BigDecimal getDespesas() {
        return despesas;
    }

    public void setDespesas(BigDecimal despesas) {
        this.despesas = despesas;
    }
}
