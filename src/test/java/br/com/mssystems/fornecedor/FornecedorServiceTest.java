package br.com.mssystems.fornecedor;

import br.com.mssystems.shared.exception.ConflitoException;
import br.com.mssystems.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

    private static final FornecedorRequest REQUEST = new FornecedorRequest(
            "Agro Sul Ltda", "Agro Sul", "11.222.333/0001-81", "contato@agrosul.com.br", "62999990000");

    @Mock
    private FornecedorRepository repository;

    @InjectMocks
    private FornecedorService service;

    @Test
    void criaFornecedorNormalizandoOCnpj() {
        when(repository.existsByCnpj("11222333000181")).thenReturn(false);
        when(repository.save(any(Fornecedor.class))).thenAnswer(inv -> inv.getArgument(0));

        FornecedorResponse resposta = service.criar(REQUEST);

        assertThat(resposta.cnpj()).isEqualTo("11222333000181");
        assertThat(resposta.razaoSocial()).isEqualTo("Agro Sul Ltda");
        assertThat(resposta.ativo()).isTrue();
    }

    @Test
    void rejeitaCnpjDuplicado() {
        when(repository.existsByCnpj("11222333000181")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(REQUEST)).isInstanceOf(ConflitoException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void buscaDeFornecedorInexistenteRetornaNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(99L)).isInstanceOf(RecursoNaoEncontradoException.class);
    }
}
