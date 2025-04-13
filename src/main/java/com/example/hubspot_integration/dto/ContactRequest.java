package com.example.hubspot_integration.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Representa a requisição para criar um novo contato.
 */
@Data
public class ContactRequest {

    /**
     * Endereço de email do contato. É obrigatório e deve ter um formato válido.
     */
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    /**
     * Primeiro nome do contato. É obrigatório e deve ter entre 2 e 50 caracteres.
     */
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
    private String firstname;

    /**
     * Sobrenome do contato. Pode ser nulo ou ter no máximo 50 caracteres.
     */
    @Size(max = 50, message = "O sobrenome não pode ter mais de 50 caracteres")
    private String lastname;
}