package dev.stefrancisquini.forumhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record UsuarioRequestDTO(
    @NotBlank(message = "Nome não pode ser vazio") String nome,
    @NotBlank(message = "Email não pode ser vazio") @Email(message = "Email inválido") String email,
    @NotBlank(message = "Senha não pode ser vazia") @Size(min = 6, message = "Senha deve ter ao menos 6 caracteres") String senha,
    Set<String> perfis
) {}

