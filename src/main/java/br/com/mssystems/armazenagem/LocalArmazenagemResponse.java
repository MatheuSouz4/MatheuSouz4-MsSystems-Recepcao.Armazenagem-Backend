package br.com.mssystems.armazenagem;

import java.math.BigDecimal;

public record LocalArmazenagemResponse(
        Long id,
        TipoLocal tipo,
        String rua,
        String posicao,
        BigDecimal capacidadeMaxima,
        boolean ativo) {

    public static LocalArmazenagemResponse from(LocalArmazenagem l) {
        return new LocalArmazenagemResponse(l.getId(), l.getTipo(), l.getRua(), l.getPosicao(),
                l.getCapacidadeMaxima(), l.isAtivo());
    }
}
