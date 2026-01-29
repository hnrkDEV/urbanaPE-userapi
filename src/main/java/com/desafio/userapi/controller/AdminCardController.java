package com.desafio.userapi.controller;

import com.desafio.userapi.dto.CardDTO;
import com.desafio.userapi.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminCardController {

    private final CardService cardService;

    public AdminCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(
            summary = "Listar todos os cartões",
            description = "Retorna todos os cartões cadastrados no sistema, independentemente do usuário. Acesso exclusivo para administradores."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cartões retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = CardDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<List<CardDTO>> listAll() {
        return ResponseEntity.ok(cardService.listAll());
    }

    @Operation(
            summary = "Listar cartões por usuário",
            description = "Retorna todos os cartões vinculados a um usuário específico. Acesso exclusivo para administradores."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cartões do usuário retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = CardDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardDTO>> listByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(cardService.listByUserAdmin(userId));
    }

    @Operation(
            summary = "Ativar ou desativar cartão",
            description = "Alterna o status (ativo/inativo) de um cartão. Ação administrativa."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status do cartão alterado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Long id) {
        cardService.toggleStatusAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remover cartão",
            description = "Remove definitivamente um cartão do sistema. Ação administrativa."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cartão removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
