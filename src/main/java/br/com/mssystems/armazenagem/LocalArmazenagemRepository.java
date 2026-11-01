package br.com.mssystems.armazenagem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalArmazenagemRepository extends JpaRepository<LocalArmazenagem, Long> {

    boolean existsByTipoAndRuaAndPosicao(TipoLocal tipo, String rua, String posicao);

    boolean existsByTipoAndRuaAndPosicaoAndIdNot(TipoLocal tipo, String rua, String posicao, Long id);
}
