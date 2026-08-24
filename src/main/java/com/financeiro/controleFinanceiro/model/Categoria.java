package com.financeiro.controleFinanceiro.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria disponíveis para uma movimentação financeira")
public enum Categoria {
    ALIMENTACAO,
    TRANSPORTE,
    MORADIA,
    LAZER,
    SAUDE,
    EDUCACAO,
    SALARIO,
    OUTROS
}