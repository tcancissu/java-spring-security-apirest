package br.com.forum_hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(@NotBlank String email);

    Optional<Usuario> findByEmailIgnoreCaseAndVerificadoTrue(String email);

    Optional<Usuario> findByToken(String codigo);

    Optional<Usuario> findByNomeUsuarioIgnoreCaseAndVerificadoTrueAndAtivoTrue(String nomeUsuario);

    Page<Usuario> findAllByAtivoTrue(Pageable paginacao);

}