package io.acordi.msbiblioteca.domain.services;

import io.acordi.msbiblioteca.entrypoint.dto.MembroDTO;
import io.acordi.msbiblioteca.domain.exception.ResourceNotFoundException;
import io.acordi.msbiblioteca.domain.entities.Membro;
import io.acordi.msbiblioteca.infra.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembroService {

    @Autowired
    private MembroRepository membroRepository;

    public List<MembroDTO> findAll() {
        return membroRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MembroDTO findById(Integer id) {
        Membro membro = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com id: " + id));
        return convertToDto(membro);
    }

    public List<MembroDTO> findByNome(String nome) {
        return membroRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MembroDTO findByEmail(String email) {
        Membro membro = membroRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com email: " + email));
        return convertToDto(membro);
    }

    public List<MembroDTO> findMembrosComMaisEmprestimos() {
        return membroRepository.findMembrosComMaisEmprestimos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MembroDTO> findMembrosComMaisMultas() {
        return membroRepository.findMembrosComMaisMultas().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MembroDTO> findMembrosComEmprestimosCompletos() {
        return membroRepository.findMembrosComEmprestimosCompletos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MembroDTO> findMembrosComEmprestimosAtivos(Integer quantidade) {
        return membroRepository.findMembrosComEmprestimosAtivos(quantidade).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MembroDTO save(MembroDTO membroDTO) {
        Membro membro = convertToEntity(membroDTO);
        Membro savedMembro = membroRepository.save(membro);
        return convertToDto(savedMembro);
    }

    @Transactional
    public MembroDTO update(Integer id, MembroDTO membroDTO) {
        Membro existingMembro = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com id: " + id));

        existingMembro.setNome(membroDTO.getNome());
        existingMembro.setEmail(membroDTO.getEmail());

        Membro updatedMembro = membroRepository.save(existingMembro);
        return convertToDto(updatedMembro);
    }

    @Transactional
    public void delete(Integer id) {
        Membro membro = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com id: " + id));

        membroRepository.delete(membro);
    }

    // Métodos de conversão entre Entity e DTO
    private MembroDTO convertToDto(Membro membro) {
        MembroDTO membroDTO = new MembroDTO();
        membroDTO.setId(membro.getId());
        membroDTO.setNome(membro.getNome());
        membroDTO.setEmail(membro.getEmail());
        return membroDTO;
    }

    private Membro convertToEntity(MembroDTO membroDTO) {
        Membro membro = new Membro();

        if (membroDTO.getId() != null) {
            membro.setId(membroDTO.getId());
        }

        membro.setNome(membroDTO.getNome());
        membro.setEmail(membroDTO.getEmail());

        return membro;
    }
}

