package com.financeiro.controleFinanceiro.controller;

import com.financeiro.controleFinanceiro.model.Movimentacao;
import com.financeiro.controleFinanceiro.model.ResumoFinanceiro;
import com.financeiro.controleFinanceiro.sevice.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//Crud
@RestController
@RequestMapping("/movimentacoes")
@CrossOrigin(origins = "*")
public class MovimentacaoController {

    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service){
        this.service = service;
    }

    @PostMapping
    public Movimentacao criar(@Valid @RequestBody Movimentacao movimentacao){
        return service.salvar(movimentacao);
    }

    @GetMapping
    public List<Movimentacao> listar(){
        return service.listar();
    }

    @GetMapping("/saldo")
    public Double saldo(){
        return service.calcularSaldo();
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id){
        service.excluir(id);
    }

    @PutMapping("/{id}")
    public Movimentacao atualizar(@PathVariable Long id, @Valid @RequestBody Movimentacao movimentacao){
        return service.atualizar(id, movimentacao);
    }

    @GetMapping("/resumo")
    public ResumoFinanceiro resumo(){
        return service.gerarResumo();
    }
}
