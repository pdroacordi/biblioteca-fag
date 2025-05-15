package io.acordi.msbiblioteca.entrypoint.controller;

import io.acordi.msbiblioteca.entrypoint.dto.EmprestimoDTO;
import io.acordi.msbiblioteca.domain.services.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@Tag(name = "Empréstimos", description = "API para gerenciamento de empréstimos de livros")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping
    @Operation(summary = "Listar todos os empréstimos", description = "Retorna uma lista com todos os empréstimos cadastrados")
    public ResponseEntity<List<EmprestimoDTO>> findAll() {
        return ResponseEntity.ok(emprestimoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empréstimo por ID", description = "Retorna um empréstimo específico pelo seu ID")
    public ResponseEntity<EmprestimoDTO> findById(
            @Parameter(description = "ID do empréstimo", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok(emprestimoService.findById(id));
    }

    @GetMapping("/membro/{membroId}")
    @Operation(summary = "Buscar empréstimos por membro", description = "Retorna uma lista de empréstimos de um determinado membro")
    public ResponseEntity<List<EmprestimoDTO>> findByMembro(
            @Parameter(description = "ID do membro", required = true)
            @PathVariable Integer membroId) {
        return ResponseEntity.ok(emprestimoService.findByMembro(membroId));
    }

    @GetMapping("/livro/{livroId}")
    @Operation(summary = "Buscar empréstimos por livro", description = "Retorna uma lista de empréstimos de um determinado livro")
    public ResponseEntity<List<EmprestimoDTO>> findByLivro(
            @Parameter(description = "ID do livro", required = true)
            @PathVariable Integer livroId) {
        return ResponseEntity.ok(emprestimoService.findByLivro(livroId));
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar empréstimos ativos", description = "Retorna uma lista de empréstimos que ainda não foram devolvidos")
    public ResponseEntity<List<EmprestimoDTO>> findEmprestimosAtivos() {
        return ResponseEntity.ok(emprestimoService.findEmprestimosAtivos());
    }

    @GetMapping("/atrasados")
    @Operation(summary = "Listar empréstimos atrasados", description = "Retorna uma lista de empréstimos que estão atrasados (não devolvidos após a data prevista)")
    public ResponseEntity<List<EmprestimoDTO>> findEmprestimosAtrasados() {
        return ResponseEntity.ok(emprestimoService.findEmprestimosAtrasados());
    }

    @GetMapping("/periodo-devolucao")
    @Operation(summary = "Buscar empréstimos por período de devolução", description = "Retorna uma lista de empréstimos devolvidos dentro do período especificado")
    public ResponseEntity<List<EmprestimoDTO>> findByDataDevolucaoBetween(
            @Parameter(description = "Data inicial", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return ResponseEntity.ok(emprestimoService.findByDataDevolucaoBetween(dataInicio, dataFim));
    }

    @GetMapping("/ativos/membro/{membroId}")
    @Operation(summary = "Listar empréstimos ativos por membro", description = "Retorna uma lista de empréstimos ativos de um determinado membro")
    public ResponseEntity<List<EmprestimoDTO>> findEmprestimosAtivosByMembro(
            @Parameter(description = "ID do membro", required = true)
            @PathVariable Integer membroId) {
        return ResponseEntity.ok(emprestimoService.findEmprestimosAtivosByMembro(membroId));
    }

    @GetMapping("/count/ativos/membro/{membroId}")
    @Operation(summary = "Contar empréstimos ativos por membro", description = "Retorna a quantidade de empréstimos ativos de um determinado membro")
    public ResponseEntity<Integer> countEmprestimosAtivosByMembro(
            @Parameter(description = "ID do membro", required = true)
            @PathVariable Integer membroId) {
        return ResponseEntity.ok(emprestimoService.countEmprestimosAtivosByMembro(membroId));
    }

    @GetMapping("/historico/livro/{livroId}")
    @Operation(summary = "Listar histórico de empréstimos de um livro", description = "Retorna uma lista com o histórico de empréstimos de um determinado livro")
    public ResponseEntity<List<EmprestimoDTO>> findEmprestimosHistoricoByLivro(
            @Parameter(description = "ID do livro", required = true)
            @PathVariable Integer livroId) {
        return ResponseEntity.ok(emprestimoService.findEmprestimosHistoricoByLivro(livroId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar empréstimo", description = "Registra um novo empréstimo de livro")
    public EmprestimoDTO save(
            @Parameter(description = "Dados do empréstimo", required = true)
            @Valid @RequestBody EmprestimoDTO emprestimoDTO) {
        return emprestimoService.save(emprestimoDTO);
    }

    @PutMapping("/{id}/devolucao")
    @Operation(summary = "Registrar devolução", description = "Registra a devolução de um livro emprestado")
    public ResponseEntity<EmprestimoDTO> registrarDevolucao(
            @Parameter(description = "ID do empréstimo", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Data de devolução (opcional, padrão é a data atual)", required = false)
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDevolucao) {
        return ResponseEntity.ok(emprestimoService.registrarDevolucao(id, dataDevolucao));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empréstimo", description = "Atualiza os dados de um empréstimo existente")
    public ResponseEntity<EmprestimoDTO> update(
            @Parameter(description = "ID do empréstimo", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Novos dados do empréstimo", required = true)
            @Valid @RequestBody EmprestimoDTO emprestimoDTO) {
        return ResponseEntity.ok(emprestimoService.update(id, emprestimoDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir empréstimo", description = "Exclui um empréstimo pelo seu ID")
    public void delete(
            @Parameter(description = "ID do empréstimo", required = true)
            @PathVariable Integer id) {
        emprestimoService.delete(id);
    }
}

