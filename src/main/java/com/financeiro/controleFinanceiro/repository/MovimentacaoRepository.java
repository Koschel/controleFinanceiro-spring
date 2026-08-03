package com.financeiro.controleFinanceiro.repository;

import com.financeiro.controleFinanceiro.model.Movimentacao;
import com.financeiro.controleFinanceiro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long>{
    List<Movimentacao> findByUsuario(Usuario usuario);
}
