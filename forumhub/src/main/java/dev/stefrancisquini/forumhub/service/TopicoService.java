package dev.stefrancisquini.forumhub.service;

import dev.stefrancisquini.forumhub.dto.*;
import dev.stefrancisquini.forumhub.entity.*;
import dev.stefrancisquini.forumhub.repository.TopicoRepository;
import dev.stefrancisquini.forumhub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicoService {
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public TopicoResponseDTO criarTopico(TopicoRequestDTO dto) {
        Optional<Topico> existente = topicoRepository.findByTituloAndMensagem(dto.getTitulo(), dto.getMensagem());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Tópico duplicado: título e mensagem já existem.");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        Topico topico = Topico.builder()
                .titulo(dto.getTitulo())
                .mensagem(dto.getMensagem())
                .dataCriacao(LocalDateTime.now())
                .status(StatusTopico.ATIVO)
                .autor(usuario.getEmail())
                .curso(dto.getCurso())
                .build();
        topico = topicoRepository.save(topico);
        return toResponseDTO(topico);
    }

    public TopicoListDTO listarTopicos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCriacao").descending());
        Page<Topico> topicos = topicoRepository.findAll(pageable);
        TopicoListDTO listDTO = new TopicoListDTO();
        listDTO.setContent(topicos.getContent().stream().map(this::toResponseDTO).collect(Collectors.toList()));
        listDTO.setTotalElements(topicos.getTotalElements());
        listDTO.setTotalPages(topicos.getTotalPages());
        listDTO.setNumber(topicos.getNumber());
        listDTO.setSize(topicos.getSize());
        return listDTO;
    }

    public TopicoResponseDTO buscarPorId(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        return toResponseDTO(topico);
    }

    @Transactional
    public TopicoResponseDTO atualizarTopico(Long id, TopicoRequestDTO dto) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!topico.getAutor().equals(email)) {
            throw new RuntimeException("Você não tem permissão para modificar este tópico");
        }
        Optional<Topico> existente = topicoRepository.findByTituloAndMensagem(dto.getTitulo(), dto.getMensagem());
        if (existente.isPresent() && !existente.get().getId().equals(id)) {
            throw new IllegalArgumentException("Tópico duplicado: título e mensagem já existem.");
        }
        topico.setTitulo(dto.getTitulo());
        topico.setMensagem(dto.getMensagem());
        topico.setCurso(dto.getCurso());
        topico = topicoRepository.save(topico);
        return toResponseDTO(topico);
    }

    @Transactional
    public void excluirTopico(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!topico.getAutor().equals(email)) {
            throw new RuntimeException("Você não tem permissão para excluir este tópico");
        }
        topicoRepository.delete(topico);
    }

    private TopicoResponseDTO toResponseDTO(Topico topico) {
        TopicoResponseDTO dto = new TopicoResponseDTO();
        Usuario autor = usuarioRepository.findByEmail(topico.getAutor())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
        dto.setId(topico.getId());
        dto.setTitulo(topico.getTitulo());
        dto.setMensagem(topico.getMensagem());
        dto.setDataCriacao(topico.getDataCriacao());
        dto.setStatus(topico.getStatus());
        dto.setAutor(autor.getNome());
        dto.setCurso(topico.getCurso());
        return dto;
    }
}
