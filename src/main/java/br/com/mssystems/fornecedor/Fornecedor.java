package br.com.mssystems.fornecedor;

import br.com.mssystems.shared.domain.AuditoriaBase;
import br.com.mssystems.shared.util.Textos;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Entity
@Table(name = "fornecedor")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fornecedor extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false, length = 200)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 200)
    private String nomeFantasia;

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false)
    private boolean ativo = true;

    public Fornecedor(String razaoSocial, String nomeFantasia, String cnpj, String email, String telefone) {
        aplicar(razaoSocial, nomeFantasia, cnpj, email, telefone);
    }

    public void atualizar(String razaoSocial, String nomeFantasia, String cnpj, String email, String telefone) {
        aplicar(razaoSocial, nomeFantasia, cnpj, email, telefone);
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    /** Remove máscara e deixa em maiúsculas (aceita CNPJ numérico e alfanumérico). */
    public static String normalizarCnpj(String cnpj) {
        return cnpj.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
    }

    private void aplicar(String razaoSocial, String nomeFantasia, String cnpj, String email, String telefone) {
        this.razaoSocial = razaoSocial.trim();
        this.nomeFantasia = Textos.limparOpcional(nomeFantasia);
        this.cnpj = normalizarCnpj(cnpj);
        this.email = Textos.limparOpcional(email);
        this.telefone = Textos.limparOpcional(telefone);
    }
}
