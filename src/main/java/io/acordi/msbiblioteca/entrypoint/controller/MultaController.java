package io.acordi.msbiblioteca.entrypoint.controller;

import io.acordi.msbiblioteca.entrypoint.dto.MultaDTO;
import io.acordi.msbiblioteca.domain.services.MultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/multas")
@Tag(name = "Multas", description = "API para gerenciamento de multas por atraso de devolução")
public class MultaController {

    @Autowired
    private MultaService multaService;

    @GetMapping
    @Operation(summary = "Listar todas as multas", description = "Retorna uma lista com todas as multas cadastradas")
    public ResponseEntity<List<MultaDTO>> findAll() {
        return ResponseEntity.ok(multaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar multa por ID", description = "Retorna uma multa específica pelo seu ID")
    public ResponseEntity<MultaDTO> findById(
            @Parameter(description = "ID da multa", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok(multaService.findById(id));
    }

    @GetMapping("/membro/{membroId}")
    @Operation(summary = "Buscar multas por membro", description = "Retorna uma lista de multas de um determinado membro")
    public ResponseEntity<List<MultaDTO>> findByMembro(
            @Parameter(description = "ID do membro", required = true)
            @PathVariable Integer membroId) {
        return ResponseEntity.ok(multaService.findByMembro(membroId));
    }

    @GetMapping("/emprestimo/{emprestimoId}")
    @Operation(summary = "Buscar multas por empréstimo", description = "Retorna uma lista de multas de um determinado empréstimo")
    public ResponseEntity<List<MultaDTO>> findByEmprestimo(
            @Parameter(description = "ID do empréstimo", required = true)
            @PathVariable Integer emprestimoId) {
        return ResponseEntity.ok(multaService.findByEmprestimo(emprestimoId));
    }

    @GetMapping("/total/membro/{membroId}")
    @Operation(summary = "Calcular total de multas por membro", description = "Retorna o valor total de multas de um determinado membro")
    public ResponseEntity<BigDecimal> getTotalMultasByMembro(
            @Parameter(description = "ID do membro", required = true)
            @PathVariable Integer membroId) {
        return ResponseEntity.ok(multaService.getTotalMultasByMembro(membroId));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Buscar multas por período", description = "Retorna uma lista de multas geradas dentro do período especificado")
    public ResponseEntity<List<MultaDTO>> findByDataGeracaoBetween(
            @Parameter(description = "Data inicial", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @Parameter(description = "Data final", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        return ResponseEntity.ok(multaService.findByDataGeracaoBetween(dataInicio, dataFim));
    }

    @GetMapping("/valor-maior")
    @Operation(summary = "Buscar multas acima de um valor", description = "Retorna uma lista de multas com valor acima do especificado")
    public ResponseEntity<List<MultaDTO>> findByValorGreaterThan(
            @Parameter(description = "Valor mínimo", required = true)
            @RequestParam BigDecimal valorMinimo) {
        return ResponseEntity.ok(multaService.findByValorGreaterThan(valorMinimo));
    }

    @GetMapping("/maiores-valores")
    @Operation(summary = "Listar multas por valor decrescente", description = "Retorna uma lista de multas ordenadas pelo valor (do maior para o menor)")
    public ResponseEntity<List<MultaDTO>> findAllOrderByValorDesc() {
        return ResponseEntity.ok(multaService.findAllOrderByValorDesc());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar multa", description = "Registra uma nova multa")
    public MultaDTO save(
            @Parameter(description = "Dados da multa", required = true)
            @Valid @RequestBody MultaDTO multaDTO) {
        return multaService.save(multaDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar multa", description = "Atualiza os dados de uma multa existente")
    public ResponseEntity<MultaDTO> update(
            @Parameter(description = "ID da multa", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Novos dados da multa", required = true)
            @Valid @RequestBody MultaDTO multaDTO) {
        return ResponseEntity.ok(multaService.update(id, multaDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir multa", description = "Exclui uma multa pelo seu ID")
    public void delete(
            @Parameter(description = "ID da multa", required = true)
            @PathVariable Integer id) {
        multaService.delete(id);
    }
}

