package com.financeiro.controleFinanceiro.repository;

import com.financeiro.controleFinanceiro.model.Movimentacao;
import com.financeiro.controleFinanceiro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long>{
    List<Movimentacao> findByUsuario(Usuario usuario);

    Optional<Movimentacao> findByIdAndUsuario(Long id, Usuario usuario);
}
