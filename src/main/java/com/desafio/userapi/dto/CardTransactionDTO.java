package com.desafio.userapi.dto;

import com.desafio.userapi.enums.TransactionType;

import java.time.LocalDateTime;

public class CardTransactionDTO {

    private Long id;
    private TransactionType type;
    private Double valor;
    private Double saldoAnterior;
    private Double saldoAtual;
    private LocalDateTime createdAt;

    // getters e setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TransactionType getType() {
        return type;
    }
    public void setType(TransactionType type) {
        this.type = type;
    }
    public Double getValor() {
        return valor;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }
    public Double getSaldoAnterior() {
        return saldoAnterior;
    }
    public void setSaldoAnterior(Double saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }
    public Double getSaldoAtual() {
        return saldoAtual;
    }
    public void setSaldoAtual(Double saldoAtual) {
        this.saldoAtual = saldoAtual;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
