package br.com.mssystems.armazenagem;

import br.com.mssystems.shared.exception.ConflitoException;
import br.com.mssystems.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocalArmazenagemService {

    private final LocalArmazenagemRepository repository;

    @Transactional
    public LocalArmazenagemResponse criar(LocalArmazenagemRequest request) {
        String rua = request.rua().trim();
        String posicao = request.posicao().trim();
        if (repository.existsByTipoAndRuaAndPosicao(request.tipo(), rua, posicao)) {
            throw new ConflitoException("Já existe um local com este tipo, rua e posição.");
        }
        LocalArmazenagem local = new LocalArmazenagem(request.tipo(), rua, posicao, request.capacidadeMaxima());
        return LocalArmazenagemResponse.from(repository.save(local));
    }

    public Page<LocalArmazenagemResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(LocalArmazenagemResponse::from);
    }

    public LocalArmazenagemResponse buscar(Long id) {
        return LocalArmazenagemResponse.from(obter(id));
    }

    @Transactional
    public LocalArmazenagemResponse atualizar(Long id, LocalArmazenagemRequest request) {
        LocalArmazenagem local = obter(id);
        String rua = request.rua().trim();
        String posicao = request.posicao().trim();
        if (repository.existsByTipoAndRuaAndPosicaoAndIdNot(request.tipo(), rua, posicao, id)) {
            throw new ConflitoException("Já existe outro local com este tipo, rua e posição.");
        }
        local.atualizar(request.tipo(), rua, posicao, request.capacidadeMaxima());
        return LocalArmazenagemResponse.from(local);
    }

    @Transactional
    public void inativar(Long id) {
        obter(id).inativar();
    }

    @Transactional
    public void ativar(Long id) {
        obter(id).ativar();
    }

    private LocalArmazenagem obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Local de armazenagem não encontrado: " + id));
    }
}
