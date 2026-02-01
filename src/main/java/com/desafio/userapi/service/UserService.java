package com.desafio.userapi.service;

import com.desafio.userapi.dto.CreateUserDTO;
import com.desafio.userapi.dto.UpdateUserDTO;
import com.desafio.userapi.dto.UserResponseDTO;
import com.desafio.userapi.entity.User;
import com.desafio.userapi.enums.Role;
import com.desafio.userapi.repository.UserRepository;
import com.desafio.userapi.repository.projections.UserCardCountProjection;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.desafio.userapi.exception.BusinessException;
import com.desafio.userapi.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public UserResponseDTO update(Long id, UpdateUserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (dto.getEmail() != null &&
                !dto.getEmail().equals(user.getEmail()) &&
                userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("Email já cadastrado");
        }

        if (dto.getNome() != null) {
            user.setNome(dto.getNome());
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        User updatedUser = userRepository.save(user);

        return toResponseDTO(updatedUser);
    }

    public UserResponseDTO getMe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return toResponseDTO(user);
    }

    public UserResponseDTO updateMe(String email, UpdateUserDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (dto.getNome() != null) {
            user.setNome(dto.getNome());
        }

        if (dto.getEmail() != null &&
                !dto.getEmail().equals(user.getEmail()) &&
                userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("Email já cadastrado");
        }

        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        return toResponseDTO(userRepository.save(user));
    }

    public List<UserCardCountProjection> getUsersWithCardCount() {
        return userRepository.findUsersWithCardCount();
    }

    public UserResponseDTO create(CreateUserDTO dto) {
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new BusinessException("Email já cadastrado");
                });

        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        user.setRole(Role.CLIENT);

        User savedUser = userRepository.save(user);

        return toResponseDTO(savedUser);
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
