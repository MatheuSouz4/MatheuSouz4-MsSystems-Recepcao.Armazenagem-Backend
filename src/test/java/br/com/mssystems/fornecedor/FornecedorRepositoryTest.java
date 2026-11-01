package br.com.mssystems.fornecedor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Teste de integração contra um PostgreSQL real (container descartável).
 * Também valida a migration V1 do Flyway e o ddl-auto=validate. Requer o Docker em execução.
 */
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@Transactional
class FornecedorRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17");

    @Autowired
    private FornecedorRepository repository;

    @Test
    void persisteEEncontraPorCnpj() {
        Fornecedor salvo = repository.saveAndFlush(novo("11222333000181"));

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getCriadoEm()).isNotNull();
        assertThat(repository.existsByCnpj("11222333000181")).isTrue();
    }

    @Test
    void naoPermiteCnpjDuplicadoNoBanco() {
        repository.saveAndFlush(novo("11222333000181"));

        assertThatThrownBy(() -> repository.saveAndFlush(novo("11222333000181")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Fornecedor novo(String cnpj) {
        return new Fornecedor("Agro Sul Ltda", "Agro Sul", cnpj, "contato@agrosul.com.br", "62999990000");
    }
}
