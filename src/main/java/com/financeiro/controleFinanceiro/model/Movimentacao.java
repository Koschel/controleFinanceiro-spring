package com.financeiro.controleFinanceiro.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.math.BigDecimal;

@Entity
@Schema(description = "Representa uma movimentação financeira")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Identificador único da movimentação",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotBlank(message = "A descrição é obrigatório.")
    @Schema(
            description = "Descrição da movimentação",
            example = "Salário"
    )
    private String descricao;


    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor precisa ser maior que Zero (0)")
    @Schema(
            description = "Valor da movimentação",
            example = "5000.00"
    )
    private BigDecimal valor;

    @NotNull(message = "O tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Schema(
            description = "Tipo da movimentação",
            example = "RECEITA"
    )
    private TipoMovimentacao tipo;

    @NotNull(message = "Categoria é obrigatória" )
    @Enumerated(EnumType.STRING)
    @Schema(
            description = "Categoria da movimentação",
            example = "SALARIO"
    )
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @Schema(
            description = "Usuário proprietário da movimentação",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Usuario usuario;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // getters e setters
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
