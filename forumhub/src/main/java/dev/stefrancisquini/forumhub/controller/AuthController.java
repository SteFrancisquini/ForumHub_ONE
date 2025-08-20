package dev.stefrancisquini.forumhub.controller;

import dev.stefrancisquini.forumhub.dto.LoginRequestDTO;
import dev.stefrancisquini.forumhub.dto.LoginResponseDTO;
import dev.stefrancisquini.forumhub.service.TokenService;
import dev.stefrancisquini.forumhub.dto.UsuarioRequestDTO;
import dev.stefrancisquini.forumhub.dto.UsuarioResponseDTO;
import dev.stefrancisquini.forumhub.entity.Usuario;
import dev.stefrancisquini.forumhub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.email(),
                    loginRequest.senha()
                )
            );
            String token = tokenService.gerarToken(loginRequest.email());
            return new LoginResponseDTO(token);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }
    }

    @PostMapping("/register")
    public UsuarioResponseDTO register(@Valid @RequestBody UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .perfis(dto.perfis() == null || dto.perfis().isEmpty() ? Set.of("ROLE_USER") : dto.perfis())
                .build();
        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfis());
    }
}
