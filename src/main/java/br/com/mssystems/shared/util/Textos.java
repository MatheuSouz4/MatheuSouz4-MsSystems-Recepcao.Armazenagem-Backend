package br.com.mssystems.shared.util;

public final class Textos {

    private Textos() {
    }

    /** Retorna o texto sem espaços nas pontas, ou null se estiver vazio. */
    public static String limparOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
