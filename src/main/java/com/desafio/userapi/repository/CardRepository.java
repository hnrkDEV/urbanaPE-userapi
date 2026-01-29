package com.desafio.userapi.repository;

import com.desafio.userapi.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByUserId(Long userId);
}