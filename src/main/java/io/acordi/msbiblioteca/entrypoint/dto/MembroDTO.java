package io.acordi.msbiblioteca.entrypoint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MembroDTO {

    private Integer id;

    @NotBlank(message = "O nome do membro é obrigatório")
    @Size(max = 100, message = "O nome do membro deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O email do membro é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 150, message = "O email do membro deve ter no máximo 150 caracteres")
    private String email;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}

