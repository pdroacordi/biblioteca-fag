package io.acordi.msbiblioteca.entrypoint.controller;

import io.acordi.msbiblioteca.entrypoint.dto.AutorDTO;
import io.acordi.msbiblioteca.domain.services.AutorService;
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
@RequestMapping("/autores")
@Tag(name = "Autores", description = "API para gerenciamento de autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    @Operation(summary = "Listar todos os autores", description = "Retorna uma lista com todos os autores cadastrados")
    public ResponseEntity<List<AutorDTO>> findAll() {
        return ResponseEntity.ok(autorService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID", description = "Retorna um autor específico pelo seu ID")
    public ResponseEntity<AutorDTO> findById(
            @Parameter(description = "ID do autor", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok(autorService.findById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar autores por nome", description = "Retorna uma lista de autores que contêm o nome especificado")
    public ResponseEntity<List<AutorDTO>> findByNome(
            @Parameter(description = "Nome (ou parte do nome) do autor", required = true)
            @RequestParam String nome) {
        return ResponseEntity.ok(autorService.findByNome(nome));
    }

    @GetMapping("/mais-livros")
    @Operation(summary = "Listar autores com mais livros", description = "Retorna uma lista de autores ordenados pela quantidade de livros que possuem")
    public ResponseEntity<List<AutorDTO>> findAutoresComMaisLivros() {
        return ResponseEntity.ok(autorService.findAutoresComMaisLivros());
    }

    @GetMapping("/mais-emprestimos")
    @Operation(summary = "Listar autores com mais empréstimos", description = "Retorna uma lista de autores cujos livros foram mais emprestados")
    public ResponseEntity<List<AutorDTO>> findAutoresComMaisEmprestimos() {
        return ResponseEntity.ok(autorService.findAutoresComMaisEmprestimos());
    }

    @GetMapping("/por-ano-publicacao/{ano}")
    @Operation(summary = "Buscar autores por ano de publicação", description = "Retorna uma lista de autores que têm livros publicados no ano especificado")
    public ResponseEntity<List<AutorDTO>> findAutoresByAnoPublicacao(
            @Parameter(description = "Ano de publicação", required = true)
            @PathVariable Integer ano) {
        return ResponseEntity.ok(autorService.findAutoresByAnoPublicacao(ano));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar autor", description = "Cadastra um novo autor")
    public AutorDTO save(
            @Parameter(description = "Dados do autor", required = true)
            @Valid @RequestBody AutorDTO autorDTO) {
        return autorService.save(autorDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar autor", description = "Atualiza os dados de um autor existente")
    public ResponseEntity<AutorDTO> update(
            @Parameter(description = "ID do autor", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Novos dados do autor", required = true)
            @Valid @RequestBody AutorDTO autorDTO) {
        return ResponseEntity.ok(autorService.update(id, autorDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir autor", description = "Exclui um autor pelo seu ID")
    public void delete(
            @Parameter(description = "ID do autor", required = true)
            @PathVariable Integer id) {
        autorService.delete(id);
    }
}

