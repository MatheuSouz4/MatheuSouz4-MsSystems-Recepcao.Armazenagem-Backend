package br.com.mssystems.fornecedor;

import br.com.mssystems.shared.exception.ConflitoException;
import br.com.mssystems.shared.exception.RecursoNaoEncontradoException;
import br.com.mssystems.shared.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FornecedorService {

    private final FornecedorRepository repository;

    @Transactional
    public FornecedorResponse criar(FornecedorRequest request) {
        String cnpj = Fornecedor.normalizarCnpj(request.cnpj());
        if (repository.existsByCnpj(cnpj)) {
            throw new ConflitoException("Já existe um fornecedor cadastrado com este CNPJ.");
        }
        Fornecedor fornecedor = new Fornecedor(request.razaoSocial(), request.nomeFantasia(),
                request.cnpj(), request.email(), request.telefone());
        return FornecedorResponse.from(repository.save(fornecedor));
    }

    public Page<FornecedorResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(FornecedorResponse::from);
    }

    public FornecedorResponse buscar(Long id) {
        return FornecedorResponse.from(obter(id));
    }

    @Transactional
    public FornecedorResponse atualizar(Long id, FornecedorRequest request) {
        Fornecedor fornecedor = obter(id);
        String cnpj = Fornecedor.normalizarCnpj(request.cnpj());
        if (repository.existsByCnpjAndIdNot(cnpj, id)) {
            throw new ConflitoException("Já existe outro fornecedor cadastrado com este CNPJ.");
        }
        fornecedor.atualizar(request.razaoSocial(), request.nomeFantasia(),
                request.cnpj(), request.email(), request.telefone());
        return FornecedorResponse.from(fornecedor);
    }

    @Transactional
    public void inativar(Long id) {
        obter(id).inativar();
    }

    @Transactional
    public void ativar(Long id) {
        obter(id).ativar();
    }

    /** Usado por outros módulos (ex.: matéria-prima) que exigem fornecedor ativo. */
    public Fornecedor obterAtivo(Long id) {
        Fornecedor fornecedor = obter(id);
        if (!fornecedor.isAtivo()) {
            throw new RegraNegocioException("O fornecedor informado está inativo.");
        }
        return fornecedor;
    }

    private Fornecedor obter(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor não encontrado: " + id));
    }
}
