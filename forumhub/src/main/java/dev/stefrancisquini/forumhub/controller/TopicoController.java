package dev.stefrancisquini.forumhub.controller;

import dev.stefrancisquini.forumhub.dto.*;
import dev.stefrancisquini.forumhub.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    @Autowired
    private TopicoService topicoService;

    @PostMapping
    public ResponseEntity<TopicoResponseDTO> criarTopico(@Valid @RequestBody TopicoRequestDTO dto) {
        TopicoResponseDTO response = topicoService.criarTopico(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<TopicoListDTO> listarTopicos(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        TopicoListDTO list = topicoService.listarTopicos(page, size);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> buscarPorId(@PathVariable Long id) {
        TopicoResponseDTO response = topicoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> atualizarTopico(@PathVariable Long id,
                                                            @Valid @RequestBody TopicoRequestDTO dto) {
        TopicoResponseDTO response = topicoService.atualizarTopico(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTopico(@PathVariable Long id) {
        topicoService.excluirTopico(id);
        return ResponseEntity.noContent().build();
    }
}

