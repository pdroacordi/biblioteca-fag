package io.acordi.msbiblioteca.infra.repository;

import io.acordi.msbiblioteca.domain.entities.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Integer> {

    List<Multa> findByMembroId(Integer membroId);

    List<Multa> findByEmprestimoId(Integer emprestimoId);

    @Query("SELECT SUM(m.valor) FROM Multa m WHERE m.membro.id = :membroId")
    BigDecimal sumValorByMembroId(@Param("membroId") Integer membroId);

    @Query("SELECT m FROM Multa m WHERE m.dataGeracao BETWEEN :dataInicio AND :dataFim ORDER BY m.dataGeracao DESC")
    List<Multa> findByDataGeracaoBetween(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT m FROM Multa m WHERE m.valor > :valorMinimo ORDER BY m.valor DESC")
    List<Multa> findByValorGreaterThan(@Param("valorMinimo") BigDecimal valorMinimo);

    @Query("SELECT m FROM Multa m ORDER BY m.valor DESC")
    List<Multa> findAllOrderByValorDesc();
}

