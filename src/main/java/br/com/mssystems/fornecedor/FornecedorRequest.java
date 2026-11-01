package br.com.mssystems.fornecedor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public record FornecedorRequest(
        @NotBlank @Size(max = 200) String razaoSocial,
        @Size(max = 200) String nomeFantasia,
        @NotBlank @CNPJ String cnpj,
        @Email @Size(max = 150) String email,
        @Size(max = 20) String telefone) {
}
