package io.acordi.msbiblioteca.infra.repository;

import io.acordi.msbiblioteca.domain.entities.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Integer> {

    List<Membro> findByNomeContainingIgnoreCase(String nome);

    Optional<Membro> findByEmail(String email);

    @Query("SELECT m FROM Membro m JOIN m.emprestimos e GROUP BY m ORDER BY COUNT(e) DESC")
    List<Membro> findMembrosComMaisEmprestimos();

    @Query("SELECT m FROM Membro m JOIN m.multas mu GROUP BY m ORDER BY SUM(mu.valor) DESC")
    List<Membro> findMembrosComMaisMultas();

    @Query("SELECT m FROM Membro m WHERE SIZE(m.emprestimos) > 0 AND " +
            "(SELECT COUNT(e) FROM Emprestimo e WHERE e.membro = m AND e.dataDevolucao IS NULL) = 0")
    List<Membro> findMembrosComEmprestimosCompletos();

    @Query("SELECT m FROM Membro m WHERE (SELECT COUNT(e) FROM Emprestimo e WHERE e.membro = m AND e.dataDevolucao IS NULL) >= :quantidade")
    List<Membro> findMembrosComEmprestimosAtivos(@Param("quantidade") Integer quantidade);
}

