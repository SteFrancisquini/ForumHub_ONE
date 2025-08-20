package dev.stefrancisquini.forumhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TopicoRequestDTO {
    @NotBlank(message = "Título não pode ser vazio")
    private String titulo;

    @NotBlank(message = "Mensagem não pode ser vazia")
    private String mensagem;

    @NotBlank(message = "Curso não pode ser vazio")
    private String curso;
}

