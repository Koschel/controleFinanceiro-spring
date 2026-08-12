package com.financeiro.controleFinanceiro.controller;

import com.financeiro.controleFinanceiro.model.Usuario;
import com.financeiro.controleFinanceiro.repository.UsuarioRepository;
import com.financeiro.controleFinanceiro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService service, UsuarioRepository usuarioRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public Usuario cadastrar(@Valid @RequestBody Usuario usuario){
        return service.salvar(usuario);
    }

    @GetMapping("/me")
    public Usuario usuarioLogado(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario não encontrado."));

    }
}
