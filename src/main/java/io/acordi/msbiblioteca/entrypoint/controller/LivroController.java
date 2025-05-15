package io.acordi.msbiblioteca.entrypoint.controller;

import io.acordi.msbiblioteca.entrypoint.dto.LivroDTO;
import io.acordi.msbiblioteca.domain.types.StatusLivro;
import io.acordi.msbiblioteca.domain.services.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "API para gerenciamento de livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    @Operation(summary = "Listar todos os livros", description = "Retorna uma lista com todos os livros cadastrados")
    public ResponseEntity<List<LivroDTO>> findAll() {
        return ResponseEntity.ok(livroService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Retorna um livro específico pelo seu ID")
    public ResponseEntity<LivroDTO> findById(
            @Parameter(description = "ID do livro", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok(livroService.findById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar livros por título", description = "Retorna uma lista de livros que contêm o título especificado")
    public ResponseEntity<List<LivroDTO>> findByTitulo(
            @Parameter(description = "Título (ou parte do título) do livro", required = true)
            @RequestParam String titulo) {
        return ResponseEntity.ok(livroService.findByTitulo(titulo));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar livros por status", description = "Retorna uma lista de livros com o status especificado (disponivel ou emprestado)")
    public ResponseEntity<List<LivroDTO>> findByStatus(
            @Parameter(description = "Status do livro", required = true)
            @PathVariable StatusLivro status) {
        return ResponseEntity.ok(livroService.findByStatus(status));
    }

    @GetMapping("/ano/{ano}")
    @Operation(summary = "Buscar livros por ano de publicação", description = "Retorna uma lista de livros publicados no ano especificado")
    public ResponseEntity<List<LivroDTO>> findByAnoPublicacao(
            @Parameter(description = "Ano de publicação", required = true)
            @PathVariable Integer ano) {
        return ResponseEntity.ok(livroService.findByAnoPublicacao(ano));
    }

    @GetMapping("/autor/{autorId}")
    @Operation(summary = "Buscar livros por autor", description = "Retorna uma lista de livros de um determinado autor")
    public ResponseEntity<List<LivroDTO>> findByAutorId(
            @Parameter(description = "ID do autor", required = true)
            @PathVariable Integer autorId) {
        return ResponseEntity.ok(livroService.findByAutorId(autorId));
    }

    @GetMapping("/mais-emprestados")
    @Operation(summary = "Listar livros mais emprestados", description = "Retorna uma lista de livros ordenados pela quantidade de empréstimos")
    public ResponseEntity<List<LivroDTO>> findLivrosMaisEmprestados() {
        return ResponseEntity.ok(livroService.findLivrosMaisEmprestados());
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar livros disponíveis", description = "Retorna uma lista de livros que estão disponíveis para empréstimo")
    public ResponseEntity<List<LivroDTO>> findLivrosDisponiveis() {
        return ResponseEntity.ok(livroService.findLivrosDisponiveis());
    }

    @GetMapping("/periodo")
    @Operation(summary = "Buscar livros por período de publicação", description = "Retorna uma lista de livros publicados entre os anos especificados")
    public ResponseEntity<List<LivroDTO>> findByAnoPublicacaoBetween(
            @Parameter(description = "Ano inicial", required = true)
            @RequestParam Integer anoInicio,
            @Parameter(description = "Ano final", required = true)
            @RequestParam Integer anoFim) {
        return ResponseEntity.ok(livroService.findByAnoPublicacaoBetween(anoInicio, anoFim));
    }

    @GetMapping("/random")
    @Operation(summary = "Listar livros aleatórios", description = "Retorna uma lista aleatória de livros (sugestões)")
    public ResponseEntity<List<LivroDTO>> findRandomLivros(
            @Parameter(description = "Quantidade de livros para retornar", required = false)
            @RequestParam(defaultValue = "5") Integer limit) {
        return ResponseEntity.ok(livroService.findRandomLivros(limit));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar livro", description = "Cadastra um novo livro")
    public LivroDTO save(
            @Parameter(description = "Dados do livro", required = true)
            @Valid @RequestBody LivroDTO livroDTO) {
        return livroService.save(livroDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente")
    public ResponseEntity<LivroDTO> update(
            @Parameter(description = "ID do livro", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Novos dados do livro", required = true)
            @Valid @RequestBody LivroDTO livroDTO) {
        return ResponseEntity.ok(livroService.update(id, livroDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir livro", description = "Exclui um livro pelo seu ID")
    public void delete(
            @Parameter(description = "ID do livro", required = true)
            @PathVariable Integer id) {
        livroService.delete(id);
    }

}

