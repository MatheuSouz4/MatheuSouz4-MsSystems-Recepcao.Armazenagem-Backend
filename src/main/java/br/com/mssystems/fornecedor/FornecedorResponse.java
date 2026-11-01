package br.com.mssystems.fornecedor;

public record FornecedorResponse(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String cnpj,
        String email,
        String telefone,
        boolean ativo) {

    public static FornecedorResponse from(Fornecedor f) {
        return new FornecedorResponse(f.getId(), f.getRazaoSocial(), f.getNomeFantasia(),
                f.getCnpj(), f.getEmail(), f.getTelefone(), f.isAtivo());
    }
}
