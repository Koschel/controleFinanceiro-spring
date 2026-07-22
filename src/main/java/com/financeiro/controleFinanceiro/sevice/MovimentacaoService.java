package com.financeiro.controleFinanceiro.sevice;

import com.financeiro.controleFinanceiro.model.Movimentacao;
import com.financeiro.controleFinanceiro.model.ResumoFinanceiro;
import com.financeiro.controleFinanceiro.model.TipoMovimentacao;
import com.financeiro.controleFinanceiro.repository.MovimentacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository repository;

    public MovimentacaoService(MovimentacaoRepository repository){
        this.repository = repository;
    }

    public Movimentacao salvar(Movimentacao movimentacao){

        validaMovimentacao(movimentacao);

        return repository.save(movimentacao);
    }

    public List<Movimentacao> listar(){
        return repository.findAll();
    }

    public Double calcularSaldo(){
        return repository.findAll()
                .stream()
                .mapToDouble(mov -> {
                    if(mov.getTipo() ==
                    TipoMovimentacao.RECEITA){
                        return mov.getValor();
                    }
                        return  -mov.getValor();
                }).sum();
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

        double despesas = repository.findAll()
                .stream()
                .filter(m -> m.getTipo() == TipoMovimentacao.DESPESA)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        double receitas = repository.findAll()
                .stream()
                .filter(m -> m.getTipo() == TipoMovimentacao.RECEITA)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        double saldo = receitas - despesas;

        return new ResumoFinanceiro(despesas, receitas, saldo);
    }


    private void validaMovimentacao(Movimentacao movimentacao){
        if(movimentacao.getDescricao() == null || movimentacao.getDescricao().trim().isEmpty()){
            throw new IllegalArgumentException("Campo descrição é obrigatório.");
        }
        if (movimentacao.getValor() == null){
            throw new IllegalArgumentException("O campo Valor é obrigatório.");
        }
        if (movimentacao.getValor() <= 0 ){
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
