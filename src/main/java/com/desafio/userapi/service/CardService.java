package com.desafio.userapi.service;

import com.desafio.userapi.dto.CardDTO;
import com.desafio.userapi.entity.Card;
import com.desafio.userapi.entity.User;
import com.desafio.userapi.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final UserService userService;

    public CardService(CardRepository cardRepository, UserService userService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
    }

    public CardDTO addCard(Long userId, CardDTO dto) {

        User user = userService.findById(userId);

        Card card = new Card();
        card.setNome(dto.getNome());
        card.setNumeroCartao(dto.getNumeroCartao());
        card.setStatus(true);
        card.setTipoCartao(dto.getTipoCartao());
        card.setUser(user);

        card.setSaldo(dto.getTipoCartao().getSaldoInicial());
        card.setLimite(dto.getTipoCartao().getLimite());

        Card saved = cardRepository.save(card);

        return toDTO(saved);
    }

    public List<CardDTO> listByUser(Long userId) {
        return cardRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ADMIN
    public List<CardDTO> listAll() {
        return cardRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CardDTO> listByUserAdmin(Long userId) {
        return cardRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void toggleStatus(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (!card.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para alterar este cartão");
        }

        card.setStatus(!card.getStatus());
        cardRepository.save(card);
    }

    public void toggleStatusAdmin(Long cardId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        card.setStatus(!card.getStatus());
        cardRepository.save(card);
    }

    public void remove(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    public void credit(Long cardId, Double valor, boolean isAdmin) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));

        if (!card.getStatus()) {
            throw new RuntimeException("Cartão inativo");
        }

        switch (card.getTipoCartao()) {

            case ESTUDANTE:
                if (!isAdmin) {
                    throw new RuntimeException("Crédito permitido apenas pela administração");
                }
                break;

            case TRABALHADOR:
                // regra futura: crédito mensal fixo
                break;

            case COMUM:
                // sem restrições adicionais
                break;
        }

        Double novoSaldo = card.getSaldo() + valor;

        if (novoSaldo > card.getLimite()) {
            throw new RuntimeException("Limite do cartão excedido");
        }

        card.setSaldo(novoSaldo);
        cardRepository.save(card);
    }


    private CardDTO toDTO(Card card) {
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
}
