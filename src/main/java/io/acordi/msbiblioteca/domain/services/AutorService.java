package io.acordi.msbiblioteca.domain.services;

import io.acordi.msbiblioteca.entrypoint.dto.AutorDTO;
import io.acordi.msbiblioteca.domain.exception.ResourceNotFoundException;
import io.acordi.msbiblioteca.domain.entities.Autor;
import io.acordi.msbiblioteca.infra.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<AutorDTO> findAll() {
        return autorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AutorDTO findById(Integer id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id: " + id));
        return convertToDto(autor);
    }

    public List<AutorDTO> findByNome(String nome) {
        return autorRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AutorDTO> findAutoresComMaisLivros() {
        return autorRepository.findAutoresComMaisLivros().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AutorDTO> findAutoresComMaisEmprestimos() {
        return autorRepository.findAutoresComMaisEmprestimos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AutorDTO> findAutoresByAnoPublicacao(Integer ano) {
        return autorRepository.findAutoresByAnoPublicacao(ano).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AutorDTO save(AutorDTO autorDTO) {
        Autor autor = convertToEntity(autorDTO);
        Autor savedAutor = autorRepository.save(autor);
        return convertToDto(savedAutor);
    }

    @Transactional
    public AutorDTO update(Integer id, AutorDTO autorDTO) {
        Autor existingAutor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id: " + id));

        existingAutor.setNome(autorDTO.getNome());

        Autor updatedAutor = autorRepository.save(existingAutor);
        return convertToDto(updatedAutor);
    }

    @Transactional
    public void delete(Integer id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id: " + id));

        autorRepository.delete(autor);
    }

    // Métodos de conversão entre Entity e DTO
    private AutorDTO convertToDto(Autor autor) {
        AutorDTO autorDTO = new AutorDTO();
        autorDTO.setId(autor.getId());
        autorDTO.setNome(autor.getNome());
        return autorDTO;
    }

    private Autor convertToEntity(AutorDTO autorDTO) {
        Autor autor = new Autor();
        if (autorDTO.getId() != null) {
            autor.setId(autorDTO.getId());
        }
        autor.setNome(autorDTO.getNome());
        return autor;
    }
}

