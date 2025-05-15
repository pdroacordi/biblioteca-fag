package io.acordi.msbiblioteca.infra.repository;

import io.acordi.msbiblioteca.domain.entities.Livro;
import io.acordi.msbiblioteca.domain.types.StatusLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByStatus(StatusLivro status);

    List<Livro> findByAnoPublicacao(Integer anoPublicacao);

    @Query("SELECT l FROM Livro l JOIN l.autores a WHERE a.id = :autorId")
    List<Livro> findByAutorId(@Param("autorId") Integer autorId);

    @Query("SELECT l FROM Livro l JOIN l.emprestimos e GROUP BY l ORDER BY COUNT(e) DESC")
    List<Livro> findLivrosMaisEmprestados();

    @Query("SELECT l FROM Livro l WHERE l.id NOT IN (SELECT e.livro.id FROM Emprestimo e WHERE e.dataDevolucao IS NULL)")
    List<Livro> findLivrosDisponiveis();

    @Query("SELECT l FROM Livro l WHERE l.anoPublicacao BETWEEN :anoInicio AND :anoFim")
    List<Livro> findByAnoPublicacaoBetween(@Param("anoInicio") Integer anoInicio, @Param("anoFim") Integer anoFim);

    @Query(value = "SELECT * FROM Livro ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Livro> findRandomLivros(@Param("limit") Integer limit);
}
