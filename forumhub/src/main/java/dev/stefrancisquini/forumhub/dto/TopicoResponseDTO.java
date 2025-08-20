package dev.stefrancisquini.forumhub.dto;

import dev.stefrancisquini.forumhub.entity.StatusTopico;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TopicoResponseDTO {
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataCriacao;
    private StatusTopico status;
    private String autor;
    private String curso;
}

