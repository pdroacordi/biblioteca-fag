package io.acordi.msbiblioteca.entrypoint.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class EmprestimoDTO {

    private Integer id;

    @NotNull(message = "O ID do membro é obrigatório")
    private Integer membroId;

    @NotNull(message = "O ID do livro é obrigatório")
    private Integer livroId;

    @NotNull(message = "A data de empréstimo é obrigatória")
    private LocalDate dataEmprestimo;

    @NotNull(message = "A data de devolução prevista é obrigatória")
    @Future(message = "A data de devolução prevista deve ser futura")
    private LocalDate dataDevolucaoPrevista;

    private LocalDate dataDevolucao;

    // Dados adicionais para exibição
    private String nomeMembro;
    private String tituloLivro;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMembroId() {
        return membroId;
    }

    public void setMembroId(Integer membroId) {
        this.membroId = membroId;
    }

    public Integer getLivroId() {
        return livroId;
    }

    public void setLivroId(Integer livroId) {
        this.livroId = livroId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getNomeMembro() {
        return nomeMembro;
    }

    public void setNomeMembro(String nomeMembro) {
        this.nomeMembro = nomeMembro;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }
}

