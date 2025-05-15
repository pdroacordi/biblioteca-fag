package io.acordi.msbiblioteca.domain.services;

import io.acordi.msbiblioteca.entrypoint.dto.MultaDTO;
import io.acordi.msbiblioteca.domain.exception.ResourceNotFoundException;
import io.acordi.msbiblioteca.domain.entities.Emprestimo;
import io.acordi.msbiblioteca.domain.entities.Membro;
import io.acordi.msbiblioteca.domain.entities.Multa;
import io.acordi.msbiblioteca.infra.repository.EmprestimoRepository;
import io.acordi.msbiblioteca.infra.repository.MembroRepository;
import io.acordi.msbiblioteca.infra.repository.MultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultaService {

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private MembroRepository membroRepository;

    public List<MultaDTO> findAll() {
        return multaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MultaDTO findById(Integer id) {
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multa não encontrada com id: " + id));
        return convertToDto(multa);
    }

    public List<MultaDTO> findByMembro(Integer membroId) {
        return multaRepository.findByMembroId(membroId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MultaDTO> findByEmprestimo(Integer emprestimoId) {
        return multaRepository.findByEmprestimoId(emprestimoId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalMultasByMembro(Integer membroId) {
        return multaRepository.sumValorByMembroId(membroId);
    }

    public List<MultaDTO> findByDataGeracaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return multaRepository.findByDataGeracaoBetween(dataInicio, dataFim).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MultaDTO> findByValorGreaterThan(BigDecimal valorMinimo) {
        return multaRepository.findByValorGreaterThan(valorMinimo).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MultaDTO> findAllOrderByValorDesc() {
        return multaRepository.findAllOrderByValorDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MultaDTO save(MultaDTO multaDTO) {
        // Verificar se o empréstimo existe
        Emprestimo emprestimo = emprestimoRepository.findById(multaDTO.getEmprestimoId())
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com id: " + multaDTO.getEmprestimoId()));

        // Verificar se o membro existe
        Membro membro = membroRepository.findById(multaDTO.getMembroId())
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com id: " + multaDTO.getMembroId()));

        Multa multa = convertToEntity(multaDTO);
        multa.setEmprestimo(emprestimo);
        multa.setMembro(membro);

        // Se a data de geração não foi fornecida, usar a data atual
        if (multa.getDataGeracao() == null) {
            multa.setDataGeracao(LocalDateTime.now());
        }

        Multa savedMulta = multaRepository.save(multa);
        return convertToDto(savedMulta);
    }

    @Transactional
    public MultaDTO update(Integer id, MultaDTO multaDTO) {
        Multa existingMulta = multaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multa não encontrada com id: " + id));

        // Não permitir alterar o empréstimo ou membro de uma multa existente
        existingMulta.setValor(multaDTO.getValor());

        Multa updatedMulta = multaRepository.save(existingMulta);
        return convertToDto(updatedMulta);
    }

    @Transactional
    public void delete(Integer id) {
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multa não encontrada com id: " + id));

        multaRepository.delete(multa);
    }

    // Métodos de conversão entre Entity e DTO
    private MultaDTO convertToDto(Multa multa) {
        MultaDTO multaDTO = new MultaDTO();
        multaDTO.setId(multa.getId());
        multaDTO.setEmprestimoId(multa.getEmprestimo().getId());
        multaDTO.setMembroId(multa.getMembro().getId());
        multaDTO.setValor(multa.getValor());
        multaDTO.setDataGeracao(multa.getDataGeracao());

        // Adicionar informações extras para exibição
        multaDTO.setNomeMembro(multa.getMembro().getNome());
        multaDTO.setTituloLivro(multa.getEmprestimo().getLivro().getTitulo());

        return multaDTO;
    }

    private Multa convertToEntity(MultaDTO multaDTO) {
        Multa multa = new Multa();

        if (multaDTO.getId() != null) {
            multa.setId(multaDTO.getId());
        }

        multa.setValor(multaDTO.getValor());
        multa.setDataGeracao(multaDTO.getDataGeracao());

        return multa;
    }
}

