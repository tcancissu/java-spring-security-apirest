package br.com.forum_hub.domain.topico;

import br.com.forum_hub.domain.autenticacao.HierarquiaService;
import br.com.forum_hub.domain.curso.CursoService;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private HierarquiaService hierarquiaService;


    @Transactional
    public Topico cadastrar(DadosCadastroTopico dados, Usuario autor) {
        var curso = cursoService.buscarPeloId(dados.cursoId());
        var topico = new Topico(dados, curso, autor);
        return repository.save(topico);
    }
    public Page<DadosListagemTopico> listar(String categoria, Long idCurso, Boolean semResposta, Boolean solucionados, Pageable paginacao) {
        Specification<Topico> spec = Specification.where(TopicoSpecification.estaAberto())
                .and(TopicoSpecification.temCategoria(categoria))
                .and(TopicoSpecification.temCursoId(idCurso))
                .and(TopicoSpecification.estaSemResposta(semResposta))
                .and(TopicoSpecification.estaSolucionado(solucionados));

        Page<Topico> topicos = repository.findAll(spec, paginacao);
        return topicos.map(DadosListagemTopico::new);
    }

    @Transactional
    public Topico atualizar(DadosAtualizacaoTopico dados, Usuario logado) {
        var topico = buscarPeloId(dados.id());

        if(hierarquiaService.usuarioNaoTemPermissoes(logado, topico.getAutor(), "ROLE_MODERADOR"))
            throw new AccessDeniedException("Você não pode editar esse tópico!");

        var curso = cursoService.buscarPeloId(dados.cursoId());
        return topico.atualizarInformacoes(dados, curso);
    }

    @Transactional
    public void excluir(Long id, Usuario logado) {
        var topico = buscarPeloId(id);

        if(hierarquiaService.usuarioNaoTemPermissoes(logado, topico.getAutor(), "ROLE_MODERADOR"))
            throw new AccessDeniedException("Você não pode apagar esse tópico!");

        if (topico.getStatus() == Status.NAO_RESPONDIDO)
            repository.deleteById(id);
        else
            throw new RegraDeNegocioException("Você não pode apagar um tópico que já foi respondido.");
    }

    public Topico buscarPeloId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Tópico não encontrado!"));

    }

    @Transactional
    public void fechar(Long id) {
        var topico = buscarPeloId(id);
        topico.fechar();
    }
}
