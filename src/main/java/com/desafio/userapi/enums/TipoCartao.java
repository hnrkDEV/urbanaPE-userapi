package com.desafio.userapi.enums;

public enum TipoCartao {
    COMUM(0.0, 200.0),
    ESTUDANTE(0.0, 300.0),
    TRABALHADOR(0.0, 500.0);

    private final Double saldoInicial;
    private final Double limite;

    TipoCartao(Double saldoInicial, Double limite) {
        this.saldoInicial = saldoInicial;
        this.limite = limite;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public Double getLimite() {
        return limite;
    }
}
