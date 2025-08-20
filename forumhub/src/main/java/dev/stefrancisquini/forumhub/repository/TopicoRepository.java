package dev.stefrancisquini.forumhub.repository;

import dev.stefrancisquini.forumhub.entity.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    @Query("SELECT t FROM Topico t WHERE t.titulo = :titulo AND t.mensagem = :mensagem")
    Optional<Topico> findByTituloAndMensagem(@Param("titulo") String titulo, @Param("mensagem") String mensagem);
}

