package com.financeiro.controleFinanceiro.controller;

import com.financeiro.controleFinanceiro.model.Usuario;
import com.financeiro.controleFinanceiro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public Usuario cadastrar(@Valid @RequestBody Usuario usuario){
        return service.salvar(usuario);
    }
}
