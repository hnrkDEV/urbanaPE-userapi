package com.desafio.userapi.controller;

import com.desafio.userapi.dto.UserResponseDTO;
import com.desafio.userapi.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponseDTO> listAll() {
        return userService.findAll();
    }

    @GetMapping("/me")
    public UserResponseDTO me(Authentication authentication) {
        String email = authentication.getName();
        return userService.getMe(email);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
