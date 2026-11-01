package br.com.mssystems.materiaprima;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MateriaPrimaRepository extends JpaRepository<MateriaPrima, Long> {

    boolean existsByNomeIgnoreCaseAndFornecedorId(String nome, Long fornecedorId);

    boolean existsByNomeIgnoreCaseAndFornecedorIdAndIdNot(String nome, Long fornecedorId, Long id);

    @Override
    @EntityGraph(attributePaths = "fornecedor")
    Page<MateriaPrima> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "fornecedor")
    Optional<MateriaPrima> findById(Long id);
}
