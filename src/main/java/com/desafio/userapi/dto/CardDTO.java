package com.desafio.userapi.dto;

import com.desafio.userapi.enums.TipoCartao;

public class CardDTO {

    private Long id;
    private Long numeroCartao;
    private String nome;
    private Boolean status;
    private TipoCartao tipoCartao;
    private Double saldo;
    private Double limite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(Long numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getLimite() {
        return limite;
    }
    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Double getSaldo() {
        return saldo;
    }
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public TipoCartao getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(TipoCartao tipoCartao) {
        this.tipoCartao = tipoCartao;
    }
}
