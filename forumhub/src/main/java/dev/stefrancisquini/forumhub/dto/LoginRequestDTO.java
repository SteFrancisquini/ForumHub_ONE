package dev.stefrancisquini.forumhub.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "Email não pode ser vazio") String email,
    @NotBlank(message = "Senha não pode ser vazia") String senha
) {}
