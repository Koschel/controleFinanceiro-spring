package com.financeiro.controleFinanceiro.repository;

import com.financeiro.controleFinanceiro.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long>{

}
