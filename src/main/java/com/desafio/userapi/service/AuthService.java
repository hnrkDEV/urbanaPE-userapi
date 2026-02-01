package com.desafio.userapi.service;

import com.desafio.userapi.dto.LoginDTO;
import com.desafio.userapi.entity.User;
import com.desafio.userapi.exception.AuthenticationException;
import com.desafio.userapi.exception.BusinessException;
import com.desafio.userapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(LoginDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                       new AuthenticationException("Email ou senha inválidos")
                );

        if (!passwordEncoder.matches(dto.getSenha(), user.getSenha())) {
            throw new AuthenticationException("Email ou senha inválidos");
        }

        return user;
    }
}
