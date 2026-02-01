package com.desafio.userapi.repository;

import com.desafio.userapi.entity.CardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {

    List<CardTransaction> findByCardIdOrderByCreatedAtDesc(Long cardId);
}
