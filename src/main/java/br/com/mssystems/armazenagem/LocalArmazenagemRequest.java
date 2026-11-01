package br.com.mssystems.armazenagem;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record LocalArmazenagemRequest(
        @NotNull TipoLocal tipo,
        @NotBlank @Size(max = 20) String rua,
        @NotBlank @Size(max = 20) String posicao,
        @NotNull @Positive @Digits(integer = 11, fraction = 3) BigDecimal capacidadeMaxima) {
}
