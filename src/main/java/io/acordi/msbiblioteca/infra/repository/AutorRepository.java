package io.acordi.msbiblioteca.infra.repository;

import io.acordi.msbiblioteca.domain.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {

    List<Autor> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT a FROM Autor a JOIN a.livros l GROUP BY a ORDER BY COUNT(l) DESC")
    List<Autor> findAutoresComMaisLivros();

    @Query("SELECT a FROM Autor a JOIN a.livros l JOIN l.emprestimos e GROUP BY a ORDER BY COUNT(e) DESC")
    List<Autor> findAutoresComMaisEmprestimos();

    @Query("SELECT a FROM Autor a JOIN a.livros l WHERE l.anoPublicacao = :ano")
    List<Autor> findAutoresByAnoPublicacao(@Param("ano") Integer ano);
}

