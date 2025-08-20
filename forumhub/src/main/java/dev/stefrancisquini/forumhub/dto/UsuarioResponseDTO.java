package dev.stefrancisquini.forumhub.dto;

import java.util.Set;

public record UsuarioResponseDTO(
    Long id,
    String nome,
    String email,
    Set<String> perfis
) {}

