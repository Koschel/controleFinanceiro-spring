package com.financeiro.controleFinanceiro.service;

import com.financeiro.controleFinanceiro.model.Movimentacao;
import com.financeiro.controleFinanceiro.model.ResumoFinanceiro;
import com.financeiro.controleFinanceiro.model.TipoMovimentacao;
import com.financeiro.controleFinanceiro.model.Usuario;
import com.financeiro.controleFinanceiro.repository.MovimentacaoRepository;
import com.financeiro.controleFinanceiro.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public MovimentacaoService(MovimentacaoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public Movimentacao salvar(Movimentacao movimentacao){

        validaMovimentacao(movimentacao);

        Usuario usuario = obterUsuarioLogado();

        movimentacao.setUsuario(usuario);

        return repository.save(movimentacao);
    }

    public List<Movimentacao> listar(){

        Usuario usuario = obterUsuarioLogado();

        return repository.findByUsuario(usuario);
    }

    public BigDecimal calcularSaldo(){

        Usuario usuario =obterUsuarioLogado();

        return repository.findByUsuario(usuario)
                .stream()
                .map(mov -> {
                    if(mov.getTipo() ==
                    TipoMovimentacao.RECEITA){
                        return mov.getValor();
                    }
                        return mov.getValor().negate();
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public Movimentacao atualizar(Long id, Movimentacao novaMovimentacao){

        validaMovimentacao(novaMovimentacao);

        Movimentacao movimentacao = repository.findById(id).orElseThrow();

        movimentacao.setDescricao(novaMovimentacao.getDescricao());
        movimentacao.setValor(novaMovimentacao.getValor());
        movimentacao.setTipo(novaMovimentacao.getTipo());
        movimentacao.setCategoria(novaMovimentacao.getCategoria());

        return repository.save(movimentacao);

    }

    public ResumoFinanceiro gerarResumo(){

        Usuario usuario = obterUsuarioLogado();

        List<Movimentacao> movimentacoes = repository.findByUsuario(usuario);

        BigDecimal receitas = BigDecimal.ZERO;
        BigDecimal despesas = BigDecimal.ZERO;

        for(Movimentacao mov : movimentacoes){
            if (mov.getTipo() == TipoMovimentacao.RECEITA){
                receitas = receitas.add(mov.getValor());
            } else{
                despesas = despesas.add(mov.getValor());
            }
        }

        BigDecimal saldo = receitas.subtract(despesas);

        return new ResumoFinanceiro(despesas, receitas, saldo);
    }

    private Usuario obterUsuarioLogado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado."));
    }


    private void validaMovimentacao(Movimentacao movimentacao){
        if(movimentacao.getDescricao() == null || movimentacao.getDescricao().trim().isEmpty()){
            throw new IllegalArgumentException("Campo descrição é obrigatório.");
        }
        if (movimentacao.getValor() == null){
            throw new IllegalArgumentException("O campo Valor é obrigatório.");
        }
        if (movimentacao.getValor().compareTo(BigDecimal.ZERO) <= 0 ){
            throw new IllegalArgumentException("O campo Valor precisa ser maior que Zero (0).");
        }
        if (movimentacao.getTipo() == null){
            throw new IllegalArgumentException("O campo Tipo é obrigatório.");
        }
        if (movimentacao.getCategoria() == null){
            throw new IllegalArgumentException("O campo Categoria é obrigatório.");
        }
    }
}
