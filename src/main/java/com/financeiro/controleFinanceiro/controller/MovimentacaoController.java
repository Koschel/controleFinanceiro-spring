package com.financeiro.controleFinanceiro.controller;

import com.financeiro.controleFinanceiro.model.Movimentacao;
import com.financeiro.controleFinanceiro.model.ResumoFinanceiro;
import com.financeiro.controleFinanceiro.service.MovimentacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @Operation(
            summary = "Criar movimentação",
            description = "Criar uma nova movimentação financeira para o usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimentação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da movimentação inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping
    public Movimentacao criar(@Valid @RequestBody Movimentacao movimentacao){
        return service.salvar(movimentacao);
    }

    @Operation(
            summary = "Listar movimentações",
            description = "Retorna as movimentações financeiras do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimentações restornadas com Sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuários não autenticação")
    })
    @GetMapping
    public List<Movimentacao> listar(){
        return service.listar();
    }

    @Operation(
            summary = "Consultar saldo",
            description = "Retorna o saldo financeiro do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimentação excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Movimentações não encontrada")
    })
    @GetMapping("/saldo")
    public BigDecimal saldo(){
        return service.calcularSaldo();
    }

    @Operation(
            summary = "Excluir movimentação",
            description = "Excluir uma movimentação pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimentação excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Movimentação não encontrada")
    })
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id){
        service.excluir(id);
    }

    @Operation(
            summary = "Atulizar movimentação",
            description = "Atualiza uma movimentação existente pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimenteção atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da movimentação inválida"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Movimenteções não encontrada")
    })
    @PutMapping("/{id}")
    public Movimentacao atualizar(@PathVariable Long id, @Valid @RequestBody Movimentacao movimentacao){
        return service.atualizar(id, movimentacao);
    }

    @Operation(
            summary = "Consultar resumo financeiro",
            description = "Retorno o resumo financeiro contendo receitas, despesas e saldo do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumo financeiro retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/resumo")
    public ResumoFinanceiro resumo(){
        return service.gerarResumo();
    }
}
