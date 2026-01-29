package com.desafio.userapi.controller;

import com.desafio.userapi.dto.CreateUserDTO;
import com.desafio.userapi.dto.LoginDTO;
import com.desafio.userapi.dto.LoginResponseDTO;
import com.desafio.userapi.dto.UserResponseDTO;
import com.desafio.userapi.entity.User;
import com.desafio.userapi.service.AuthService;
import com.desafio.userapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.desafio.userapi.security.JwtService;

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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto) {

        User user = authService.authenticate(dto);

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody CreateUserDTO dto) {
        return userService.create(dto);
    }
}
