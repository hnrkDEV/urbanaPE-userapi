package com.desafio.userapi.controller;

import com.desafio.userapi.dto.CardDTO;
import com.desafio.userapi.entity.User;
import com.desafio.userapi.service.CardService;
import com.desafio.userapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;
    private final UserService userService;

    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CardDTO> addCard(
            @RequestBody @Valid CardDTO dto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        CardDTO saved = cardService.addCard(user.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<CardDTO>> listMyCards(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        return ResponseEntity.ok(cardService.listByUser(user.getId()));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleStatus(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        cardService.toggleStatus(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        cardService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
