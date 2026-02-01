package com.desafio.userapi.dto;

import com.desafio.userapi.enums.Role;

public class UpdateUserDTO {

    private String nome;
    private String email;
    private Role role;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
