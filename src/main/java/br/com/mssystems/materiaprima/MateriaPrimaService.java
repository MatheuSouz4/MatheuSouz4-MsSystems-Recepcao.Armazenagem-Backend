package br.com.mssystems.materiaprima;

import br.com.mssystems.fornecedor.Fornecedor;
import br.com.mssystems.fornecedor.FornecedorService;
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
public class MateriaPrimaService {

    private final MateriaPrimaRepository repository;
    private final FornecedorService fornecedorService;

    @Transactional
    public MateriaPrimaResponse criar(MateriaPrimaRequest request) {
        Fornecedor fornecedor = fornecedorService.obterAtivo(request.fornecedorId());
        String nome = request.nome().trim();
        if (repository.existsByNomeIgnoreCaseAndFornecedorId(nome, fornecedor.getId())) {
            throw new ConflitoException("Este fornecedor já possui uma matéria-prima com este nome.");
        }
        MateriaPrima materiaPrima = new MateriaPrima(nome, request.descricao(), request.categoria(), fornecedor);
        return MateriaPrimaResponse.from(repository.save(materiaPrima));
    }

    public Page<MateriaPrimaResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(MateriaPrimaResponse::from);
    }

    public MateriaPrimaResponse buscar(Long id) {
        return MateriaPrimaResponse.from(obter(id));
    }

    @Transactional
    public MateriaPrimaResponse atualizar(Long id, MateriaPrimaRequest request) {
        MateriaPrima materiaPrima = obter(id);
        Fornecedor fornecedor = fornecedorService.obterAtivo(request.fornecedorId());
        String nome = request.nome().trim();
        if (repository.existsByNomeIgnoreCaseAndFornecedorIdAndIdNot(nome, fornecedor.getId(), id)) {
            throw new ConflitoException("Este fornecedor já possui uma matéria-prima com este nome.");
        }
        materiaPrima.atualizar(nome, request.descricao(), request.categoria(), fornecedor);
        return MateriaPrimaResponse.from(materiaPrima);
    }

    @Transactional
    public void inativar(Long id) {
        obter(id).inativar();
    }

    @Transactional
    public void ativar(Long id) {
        obter(id).ativar();
    }

    private MateriaPrima obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria-prima não encontrada: " + id));
    }
}
