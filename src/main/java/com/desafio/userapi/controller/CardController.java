package com.desafio.userapi.controller;

import com.desafio.userapi.dto.CardDTO;
import com.desafio.userapi.entity.User;
import com.desafio.userapi.service.CardService;
import com.desafio.userapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
@SecurityRequirement(name = "bearerAuth")
public class CardController {

    private final CardService cardService;
    private final UserService userService;

    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @Operation(
            summary = "Cadastrar um novo cartão",
            description = "Permite que o usuário autenticado cadastre um novo cartão vinculado à sua conta."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cartão criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CardDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "409", description = "Cartão já cadastrado")
    })
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

    @Operation(
            summary = "Listar cartões do usuário",
            description = "Retorna todos os cartões cadastrados para o usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cartões retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = CardDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<CardDTO>> listMyCards(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        return ResponseEntity.ok(cardService.listByUser(user.getId()));
    }

    @Operation(
            summary = "Ativar ou desativar cartão",
            description = "Alterna o status (ativo/inativo) de um cartão pertencente ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status do cartão alterado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Cartão não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
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

    @Operation(
            summary = "Remover cartão",
            description = "Remove um cartão pertencente ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cartão removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Cartão não pertence ao usuário"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        cardService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
