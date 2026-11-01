package br.com.mssystems.materiaprima;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MateriaPrimaRequest(
        @NotBlank @Size(max = 150) String nome,
        @Size(max = 500) String descricao,
        @NotNull CategoriaMateriaPrima categoria,
        @NotNull Long fornecedorId) {
}
