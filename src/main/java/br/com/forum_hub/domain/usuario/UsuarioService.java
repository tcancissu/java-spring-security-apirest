package br.com.forum_hub.domain.usuario;

import br.com.forum_hub.infra.email.EmailService;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }

    @Transactional
    public Usuario cadastrar(DadosCadastroUsuario dados) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmailIgnoreCase(dados.email());
        System.out.println("Teste: " + optionalUsuario.isPresent());

        if(optionalUsuario.isPresent()){
            throw new RegraDeNegocioException("Já existe uma conta cadastrada com esse email!");
        }

        if (!dados.senha().equals(dados.confirmacaoSenha())) {
            throw new RegraDeNegocioException("Senha não bate com a confirmação!");
        }

        var senhaCriptografada = passwordEncoder.encode(dados.senha());
        var usuario = new Usuario(dados, senhaCriptografada);

        emailService.enviarEmailVerificacao(usuario);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void verificarEmail(String codigo) {
        var usuario = usuarioRepository.findByToken(codigo).orElseThrow();
        usuario.verificar();
    }

    @Transactional
    public Usuario editarPerfil(Usuario usuario, DadosEdicaoUsuario dados) {
        var usuarioAlterado = usuario.alterarDados(dados);
        return usuarioRepository.save(usuarioAlterado);
    }

    @Transactional
    public void alterarSenha(DadosAlteracaoSenha dados, Usuario logado) {
        if(!passwordEncoder.matches(dados.senhaAtual(), logado.getPassword())){
            throw new RegraDeNegocioException("Senha digitada não confere com senha atual!");
        }

        if(!dados.novaSenha().equals(dados.novaSenhaConfirmacao())){
            throw new RegraDeNegocioException("Senha e confirmação não conferem!");
        }

        String senhaCriptografada = passwordEncoder.encode(dados.novaSenha());
        logado.alterarSenha(senhaCriptografada);
        usuarioRepository.save(logado);
    }

    @Transactional
    public void desativarUsuario(Usuario logado) {
        logado.desativar();
        usuarioRepository.save(logado);
    }

    public Usuario buscarPeloNomeUsuario(String nome) {
        return usuarioRepository.findByNomeUsuarioIgnoreCaseAndVerificadoTrueAndAtivoTrue(nome).orElseThrow(
                () -> new RegraDeNegocioException("Usuário não encontrado!"));
    }

    public Page<DadosListagemUsuario> listarUsuarios(Pageable paginacao) {
        return usuarioRepository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new);
    }
}