package com.desafio.userapi.controller;

import com.desafio.userapi.dto.CardDTO;
import com.desafio.userapi.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCardController {

    private final CardService cardService;

    public AdminCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<CardDTO>> listAll() {
        return ResponseEntity.ok(cardService.listAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardDTO>> listByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(cardService.listByUserAdmin(userId));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Long id) {
        cardService.toggleStatusAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
