package br.com.mssystems.armazenagem;

import br.com.mssystems.shared.domain.AuditoriaBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "local_armazenagem")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalArmazenagem extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoLocal tipo;

    @Column(nullable = false, length = 20)
    private String rua;

    @Column(nullable = false, length = 20)
    private String posicao;

    /** Capacidade máxima em quilogramas (kg). */
    @Column(name = "capacidade_maxima", nullable = false, precision = 14, scale = 3)
    private BigDecimal capacidadeMaxima;

    @Column(nullable = false)
    private boolean ativo = true;

    public LocalArmazenagem(TipoLocal tipo, String rua, String posicao, BigDecimal capacidadeMaxima) {
        aplicar(tipo, rua, posicao, capacidadeMaxima);
    }

    public void atualizar(TipoLocal tipo, String rua, String posicao, BigDecimal capacidadeMaxima) {
        aplicar(tipo, rua, posicao, capacidadeMaxima);
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    private void aplicar(TipoLocal tipo, String rua, String posicao, BigDecimal capacidadeMaxima) {
        this.tipo = tipo;
        this.rua = rua.trim();
        this.posicao = posicao.trim();
        this.capacidadeMaxima = capacidadeMaxima;
    }
}
