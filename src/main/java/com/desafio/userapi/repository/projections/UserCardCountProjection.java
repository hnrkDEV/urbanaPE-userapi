package com.desafio.userapi.repository.projections;


public interface UserCardCountProjection {

    Long getUserId();

    String getNome();

    String getRole();

    Long getTotalCards();
}
