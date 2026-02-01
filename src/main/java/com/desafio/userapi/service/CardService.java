package com.desafio.userapi.service;

import com.desafio.userapi.dto.CardDTO;
import com.desafio.userapi.dto.CardTransactionDTO;
import com.desafio.userapi.entity.Card;
import com.desafio.userapi.entity.CardTransaction;
import com.desafio.userapi.entity.User;
import com.desafio.userapi.enums.TransactionType;
import com.desafio.userapi.exception.BusinessException;
import com.desafio.userapi.repository.CardRepository;
import com.desafio.userapi.repository.CardTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardTransactionRepository transactionRepository;
    private final UserService userService;

    public CardService(
            CardRepository cardRepository,
            CardTransactionRepository transactionRepository,
            UserService userService
    ) {
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    private Long gerarNumeroCartao() {
        return 4000_0000_0000L + (long) (Math.random() * 1_000_000_000L);
    }

    public CardDTO addCard(Long userId, CardDTO dto) {

        User user = userService.findById(userId);

        Card card = new Card();
        card.setNome(dto.getNome());
        card.setNumeroCartao(gerarNumeroCartao());
        card.setStatus(true);
        card.setTipoCartao(dto.getTipoCartao());
        card.setUser(user);
        card.setSaldo(dto.getTipoCartao().getSaldoInicial());
        card.setLimite(dto.getTipoCartao().getLimite());

        return toCardDTO(cardRepository.save(card));
    }

    public List<CardDTO> listByUser(Long userId) {
        return cardRepository.findByUserId(userId)
                .stream()
                .map(this::toCardDTO)
                .collect(Collectors.toList());
    }

    public List<CardDTO> listAll() {
        return cardRepository.findAll()
                .stream()
                .map(this::toCardDTO)
                .collect(Collectors.toList());
    }

    public void toggleStatus(Long cardId, Long userId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("Cartão não encontrado"));

        if (!card.getUser().getId().equals(userId)) {
            throw new BusinessException("Você não tem permissão para alterar este cartão");
        }

        card.setStatus(!card.getStatus());
        cardRepository.save(card);
    }

    public void toggleStatusAdmin(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("Cartão não encontrado"));

        card.setStatus(!card.getStatus());
        cardRepository.save(card);
    }

    public void credit(Long cardId, Double valor, boolean isAdmin) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("Cartão não encontrado"));

        if (!card.getStatus()) {
            throw new BusinessException("Cartão inativo");
        }

        if (valor == null || valor <= 0) {
            throw new BusinessException("Valor inválido para crédito");
        }

        validarPermissaoCredito(card, isAdmin);
        validarLimite(card, valor);

        Double saldoAnterior = card.getSaldo();
        Double saldoAtual = saldoAnterior + valor;

        card.setSaldo(saldoAtual);
        cardRepository.save(card);

        CardTransaction tx = new CardTransaction();
        tx.setCard(card);
        tx.setType(TransactionType.CREDIT);
        tx.setValor(valor);
        tx.setSaldoAnterior(saldoAnterior);
        tx.setSaldoAtual(saldoAtual);

        transactionRepository.save(tx);
    }

    public List<CardTransactionDTO> getTransactions(Long cardId, Long userId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("Cartão não encontrado"));

        if (!card.getUser().getId().equals(userId)) {
            throw new BusinessException("Acesso não permitido");
        }

        return transactionRepository
                .findByCardIdOrderByCreatedAtDesc(cardId)
                .stream()
                .map(this::toTransactionDTO)
                .collect(Collectors.toList());
    }

    private void validarPermissaoCredito(Card card, boolean isAdmin) {

        switch (card.getTipoCartao()) {

            case ESTUDANTE:
                if (!isAdmin) {
                    throw new BusinessException(
                            "Cartão estudante só pode receber crédito pela administração"
                    );
                }
                break;

            case TRABALHADOR:
                if (!isAdmin) {
                    throw new BusinessException(
                            "Crédito do cartão trabalhador é realizado automaticamente"
                    );
                }
                break;

            case COMUM:
                break;
        }
    }

    private void validarLimite(Card card, Double valor) {

        if (card.getSaldo() + valor > card.getLimite()) {
            throw new BusinessException("Limite do cartão excedido");
        }
    }

    public void remove(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("Cartão não encontrado"));

        cardRepository.delete(card);
    }

    private CardDTO toCardDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setNome(card.getNome());
        dto.setNumeroCartao(card.getNumeroCartao());
        dto.setStatus(card.getStatus());
        dto.setTipoCartao(card.getTipoCartao());
        dto.setSaldo(card.getSaldo());
        dto.setLimite(card.getLimite());
        return dto;
    }

    private CardTransactionDTO toTransactionDTO(CardTransaction tx) {
        CardTransactionDTO dto = new CardTransactionDTO();
        dto.setId(tx.getId());
        dto.setType(tx.getType());
        dto.setValor(tx.getValor());
        dto.setSaldoAnterior(tx.getSaldoAnterior());
        dto.setSaldoAtual(tx.getSaldoAtual());
        dto.setCreatedAt(tx.getCreatedAt());
        return dto;
    }
}

