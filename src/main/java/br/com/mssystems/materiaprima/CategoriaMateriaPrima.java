package br.com.mssystems.materiaprima;

import lombok.Getter;

/** O prefixo é usado na geração do código de rastreabilidade do lote (RF03.2). */
@Getter
public enum CategoriaMateriaPrima {

    ORIGEM_ANIMAL_OA("OA"),
    ORIGEM_VEGETAL_OV("OV"),
    MINERAL_MICRO_MI("MI");

    private final String prefixo;

    CategoriaMateriaPrima(String prefixo) {
        this.prefixo = prefixo;
    }
}
