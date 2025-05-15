package io.acordi.msbiblioteca.infra.repository;

import io.acordi.msbiblioteca.domain.entities.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {

    List<Emprestimo> findByMembroId(Integer membroId);

    List<Emprestimo> findByLivroId(Integer livroId);

    @Query("SELECT e FROM Emprestimo e WHERE e.dataDevolucao IS NULL")
    List<Emprestimo> findEmprestimosAtivos();

    @Query("SELECT e FROM Emprestimo e WHERE e.dataDevolucao IS NULL AND e.dataDevolucaoPrevista < :hoje")
    List<Emprestimo> findEmprestimosAtrasados(@Param("hoje") LocalDate hoje);

    @Query("SELECT e FROM Emprestimo e WHERE e.dataDevolucao >= :dataInicio AND e.dataDevolucao <= :dataFim")
    List<Emprestimo> findByDataDevolucaoBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT e FROM Emprestimo e WHERE e.membro.id = :membroId AND e.dataDevolucao IS NULL")
    List<Emprestimo> findEmprestimosAtivosByMembro(@Param("membroId") Integer membroId);

    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.membro.id = :membroId AND e.dataDevolucao IS NULL")
    Integer countEmprestimosAtivosByMembro(@Param("membroId") Integer membroId);

    @Query("SELECT e FROM Emprestimo e WHERE e.livro.id = :livroId ORDER BY e.dataEmprestimo DESC")
    List<Emprestimo> findEmprestimosHistoricoByLivro(@Param("livroId") Integer livroId);
}

