package com.financeiro.controleFinanceiro.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratamentoDeErros {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> tratarValidacao(
            MethodArgumentNotValidException ex){

        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(erros);
    }

    public ResponseEntity<List<String>> tratarRegraNegocio(IllegalArgumentException ex){
        return ResponseEntity.badRequest()
                .body(List.of(ex.getMessage()));
    }
}
