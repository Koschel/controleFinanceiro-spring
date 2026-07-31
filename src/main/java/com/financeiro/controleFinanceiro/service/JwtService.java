package com.financeiro.controleFinanceiro.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // Apenas para desenvolvimento.
    // Depois sera movido para application.properties
    private static final String SECRET = "MinhaChaveSuperSecretaParaJwtComMaisDe32Caracteres";

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getKey())
                .compact();
    }

    public String extrairEmail(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean tokenExpirado(String token){

        Date dataExpiracao = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return dataExpiracao.before(new Date());
    }

    public boolean validaToken(String token, String email){
        String emailToken = extrairEmail(token);

        return emailToken.equals(email) && !tokenExpirado(token);
    }
}
