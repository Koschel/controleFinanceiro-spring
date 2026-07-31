package com.financeiro.controleFinanceiro.security;

import com.financeiro.controleFinanceiro.service.JwtService;
import com.financeiro.controleFinanceiro.service.UsuarioDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioDetailsService usuarioDetailsService) {
        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException{

        // Obtém o valor do cabeçalho "Authorization" da requisição.
        String authHeader = request.getHeader("Authorization");

        // Verifica se o cabeçalho existe e se começa com "Bearer ".
        // Se não existir, significa que a requisição não possui um token JWT.
        // Nesse caso, apenas deixa a requisição continuar.
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Remove a palavra "Bearer " do início da String,
        // deixando apenas o token JWT.
        String jwt = authHeader.substring(7);

        // Utiliza o JwtService para ler o token e extrair o e-mail
        // armazenado no Subject (sub) do JWT.
        String email = jwtService.extrairEmail(jwt);

        // Busca o usuário no banco de dados através do e-mail
        // utilizando o UsuarioDetailsService.
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

        // Verifica se o token é válido.
        if (!jwtService.validaToken(jwt, userDetails.getUsername())){
            filterChain.doFilter(request,response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        // O próximo passo será informar ao Spring Security
        // que este usuário está autenticado.
        filterChain.doFilter(request, response);
    }
}
