package br.com.forum_hub.controller;

import br.com.forum_hub.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<DadosListagemUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder){
        var usuario = usuarioService.cadastrar(dados);
        var uri = uriBuilder.path("/{nomeUsuario}").buildAndExpand(usuario.getNomeUsuario()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));
    }

    @GetMapping("/verificar-conta")
    public ResponseEntity<String> verificarEmail(@RequestParam String codigo){
        usuarioService.verificarEmail(codigo);
        return ResponseEntity.ok("Conta verificada com sucesso!");
    }

    @PutMapping("/editar-perfil")
    public ResponseEntity<DadosListagemUsuario> editarPerfil(@RequestBody @Valid DadosEdicaoUsuario dados, @AuthenticationPrincipal Usuario logado){
        var usuario = usuarioService.editarPerfil(logado, dados);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @PatchMapping("alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid DadosAlteracaoSenha dados, @AuthenticationPrincipal Usuario logado){
        usuarioService.alterarSenha(dados, logado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/desativar")
    public ResponseEntity<Void> banirUsuario(@AuthenticationPrincipal Usuario logado){
        usuarioService.desativarUsuario(logado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar-usuarios")
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@PageableDefault(size = 10, sort = {"nomeCompleto"}) Pageable paginacao) {
        var page = usuarioService.listarUsuarios(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/usuario/{nome}")
    public ResponseEntity detalhar(@PathVariable String nome) {
        var usuario = usuarioService.buscarPeloNomeUsuario(nome);
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

}
