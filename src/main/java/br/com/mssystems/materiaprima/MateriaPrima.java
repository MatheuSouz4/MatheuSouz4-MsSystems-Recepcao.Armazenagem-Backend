package br.com.mssystems.materiaprima;

import br.com.mssystems.fornecedor.Fornecedor;
import br.com.mssystems.shared.domain.AuditoriaBase;
import br.com.mssystems.shared.util.Textos;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "materia_prima")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MateriaPrima extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaMateriaPrima categoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(nullable = false)
    private boolean ativo = true;

    public MateriaPrima(String nome, String descricao, CategoriaMateriaPrima categoria, Fornecedor fornecedor) {
        aplicar(nome, descricao, categoria, fornecedor);
    }

    public void atualizar(String nome, String descricao, CategoriaMateriaPrima categoria, Fornecedor fornecedor) {
        aplicar(nome, descricao, categoria, fornecedor);
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    private void aplicar(String nome, String descricao, CategoriaMateriaPrima categoria, Fornecedor fornecedor) {
        this.nome = nome.trim();
        this.descricao = Textos.limparOpcional(descricao);
        this.categoria = categoria;
        this.fornecedor = fornecedor;
    }
}
