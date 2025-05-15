package io.acordi.msbiblioteca.entrypoint.dto;


import io.acordi.msbiblioteca.domain.types.StatusLivro;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class LivroDTO {

    private Integer id;

    @NotBlank(message = "O título do livro é obrigatório")
    @Size(max = 200, message = "O título do livro deve ter no máximo 200 caracteres")
    private String titulo;

    @Min(value = 0, message = "O ano de publicação deve ser maior ou igual a 0")
    private Integer anoPublicacao;

    private StatusLivro status;

    private Set<AutorDTO> autores = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public StatusLivro getStatus() {
        return status;
    }

    public void setStatus(StatusLivro status) {
        this.status = status;
    }

    public Set<AutorDTO> getAutores() {
        return autores;
    }

    public void setAutores(Set<AutorDTO> autores) {
        this.autores = autores;
    }
}

