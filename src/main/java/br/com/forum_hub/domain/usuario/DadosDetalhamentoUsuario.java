package br.com.forum_hub.domain.usuario;

public record DadosDetalhamentoUsuario(Long id,
                                       String email,
                                       String nomeCompleto,
                                       String nomeUsuario,
                                       String miniBiografia,
                                       String biografia,
                                       Boolean ativo
) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getUsername(), usuario.getNomeCompleto(), usuario.getNomeUsuario(),
                usuario.getMiniBiografia(), usuario.getBiografia(), usuario.getAtivo());
    }
}