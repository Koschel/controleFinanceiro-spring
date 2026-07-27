package com.financeiro.controleFinanceiro.sevice;

import com.financeiro.controleFinanceiro.model.Usuario;
import com.financeiro.controleFinanceiro.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario salvar (Usuario usuario){

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return repository.save(usuario);

    }
}
