package io.acordi.msbiblioteca.entrypoint.controller;

import io.acordi.msbiblioteca.entrypoint.dto.MembroDTO;
import io.acordi.msbiblioteca.domain.services.MembroService;
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
@RequestMapping("/membros")
@Tag(name = "Membros", description = "API para gerenciamento de membros da biblioteca")
public class MembroController {

    @Autowired
    private MembroService membroService;

    @GetMapping
    @Operation(summary = "Listar todos os membros", description = "Retorna uma lista com todos os membros cadastrados")
    public ResponseEntity<List<MembroDTO>> findAll() {
        return ResponseEntity.ok(membroService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar membro por ID", description = "Retorna um membro específico pelo seu ID")
    public ResponseEntity<MembroDTO> findById(
            @Parameter(description = "ID do membro", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok(membroService.findById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar membros por nome", description = "Retorna uma lista de membros que contêm o nome especificado")
    public ResponseEntity<List<MembroDTO>> findByNome(
            @Parameter(description = "Nome (ou parte do nome) do membro", required = true)
            @RequestParam String nome) {
        return ResponseEntity.ok(membroService.findByNome(nome));
    }

    @GetMapping("/email")
    @Operation(summary = "Buscar membro por email", description = "Retorna um membro específico pelo seu email")
    public ResponseEntity<MembroDTO> findByEmail(
            @Parameter(description = "Email do membro", required = true)
            @RequestParam String email) {
        return ResponseEntity.ok(membroService.findByEmail(email));
    }

    @GetMapping("/mais-emprestimos")
    @Operation(summary = "Listar membros com mais empréstimos", description = "Retorna uma lista de membros ordenados pela quantidade de empréstimos")
    public ResponseEntity<List<MembroDTO>> findMembrosComMaisEmprestimos() {
        return ResponseEntity.ok(membroService.findMembrosComMaisEmprestimos());
    }

    @GetMapping("/mais-multas")
    @Operation(summary = "Listar membros com mais multas", description = "Retorna uma lista de membros ordenados pelo valor total de multas")
    public ResponseEntity<List<MembroDTO>> findMembrosComMaisMultas() {
        return ResponseEntity.ok(membroService.findMembrosComMaisMultas());
    }

    @GetMapping("/emprestimos-completos")
    @Operation(summary = "Listar membros com empréstimos completos", description = "Retorna uma lista de membros que já completaram todos os seus empréstimos")
    public ResponseEntity<List<MembroDTO>> findMembrosComEmprestimosCompletos() {
        return ResponseEntity.ok(membroService.findMembrosComEmprestimosCompletos());
    }

    @GetMapping("/emprestimos-ativos")
    @Operation(summary = "Listar membros com empréstimos ativos", description = "Retorna uma lista de membros que possuem o número especificado ou mais de empréstimos ativos")
    public ResponseEntity<List<MembroDTO>> findMembrosComEmprestimosAtivos(
            @Parameter(description = "Quantidade mínima de empréstimos ativos", required = false)
            @RequestParam(defaultValue = "1") Integer quantidade) {
        return ResponseEntity.ok(membroService.findMembrosComEmprestimosAtivos(quantidade));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar membro", description = "Cadastra um novo membro")
    public MembroDTO save(
            @Parameter(description = "Dados do membro", required = true)
            @Valid @RequestBody MembroDTO membroDTO) {
        return membroService.save(membroDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar membro", description = "Atualiza os dados de um membro existente")
    public ResponseEntity<MembroDTO> update(
            @Parameter(description = "ID do membro", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Novos dados do membro", required = true)
            @Valid @RequestBody MembroDTO membroDTO) {
        return ResponseEntity.ok(membroService.update(id, membroDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir membro", description = "Exclui um membro pelo seu ID")
    public void delete(
            @Parameter(description = "ID do membro", required = true)
            @PathVariable Integer id) {
        membroService.delete(id);
    }
}

