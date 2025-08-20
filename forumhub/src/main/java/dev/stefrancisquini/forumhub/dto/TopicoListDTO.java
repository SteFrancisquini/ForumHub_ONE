package dev.stefrancisquini.forumhub.dto;

import lombok.Data;
import java.util.List;

@Data
public class TopicoListDTO {
    private List<TopicoResponseDTO> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
}

