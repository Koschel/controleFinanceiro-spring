package com.financeiro.controleFinanceiro.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos disponíveis para uma movimentação financeira")
public enum TipoMovimentacao {
    RECEITA,
    DESPESA
}
