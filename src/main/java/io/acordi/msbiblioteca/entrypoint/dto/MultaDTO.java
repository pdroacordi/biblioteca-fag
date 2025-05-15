package io.acordi.msbiblioteca.entrypoint.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MultaDTO {

    private Integer id;

    @NotNull(message = "O ID do empréstimo é obrigatório")
    private Integer emprestimoId;

    @NotNull(message = "O ID do membro é obrigatório")
    private Integer membroId;

    @NotNull(message = "O valor da multa é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor da multa deve ser maior que zero")
    private BigDecimal valor;

    private LocalDateTime dataGeracao;

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

    public Integer getEmprestimoId() {
        return emprestimoId;
    }

    public void setEmprestimoId(Integer emprestimoId) {
        this.emprestimoId = emprestimoId;
    }

    public Integer getMembroId() {
        return membroId;
    }

    public void setMembroId(Integer membroId) {
        this.membroId = membroId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
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

