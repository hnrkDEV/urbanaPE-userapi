package com.desafio.userapi.controller;

import com.desafio.userapi.dto.CreateUserDTO;
import com.desafio.userapi.dto.LoginDTO;
import com.desafio.userapi.dto.LoginResponseDTO;
import com.desafio.userapi.dto.UserResponseDTO;
import com.desafio.userapi.entity.User;
import com.desafio.userapi.service.AuthService;
import com.desafio.userapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.desafio.userapi.security.JwtService;

@Tag(name = "Autenticação", description = "Endpoints de login e registro")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthService authService,
                          JwtService jwtService,
                          UserService userService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Operation(
            summary = "Login do usuário",
            description = "Autentica o usuário e retorna um token JWT"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto) {

        User user = authService.authenticate(dto);

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(
            summary = "Registrar o Usuário",
            description = "Registra um usuário novo na API"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody CreateUserDTO dto) {
        return userService.create(dto);
    }
}
