package br.com.mssystems.materiaprima;

public record MateriaPrimaResponse(
        Long id,
        String nome,
        String descricao,
        CategoriaMateriaPrima categoria,
        String prefixoLote,
        Long fornecedorId,
        String fornecedorRazaoSocial,
        boolean ativo) {

    public static MateriaPrimaResponse from(MateriaPrima m) {
        return new MateriaPrimaResponse(m.getId(), m.getNome(), m.getDescricao(), m.getCategoria(),
                m.getCategoria().getPrefixo(), m.getFornecedor().getId(),
                m.getFornecedor().getRazaoSocial(), m.isAtivo());
    }
}
