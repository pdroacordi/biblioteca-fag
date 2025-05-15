package io.acordi.msbiblioteca.domain.services;

import io.acordi.msbiblioteca.entrypoint.dto.LivroDTO;
import io.acordi.msbiblioteca.domain.exception.ResourceNotFoundException;
import io.acordi.msbiblioteca.domain.entities.Autor;
import io.acordi.msbiblioteca.domain.entities.Livro;
import io.acordi.msbiblioteca.domain.types.StatusLivro;
import io.acordi.msbiblioteca.infra.repository.AutorRepository;
import io.acordi.msbiblioteca.infra.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorService autorService;

    public List<LivroDTO> findAll() {
        return livroRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LivroDTO findById(Integer id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com id: " + id));
        return convertToDto(livro);
    }

    public List<LivroDTO> findByTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LivroDTO> findByStatus(StatusLivro status) {
        return livroRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LivroDTO> findByAnoPublicacao(Integer anoPublicacao) {
        return livroRepository.findByAnoPublicacao(anoPublicacao).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LivroDTO> findByAutorId(Integer autorId) {
        return livroRepository.findByAutorId(autorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LivroDTO> findLivrosMaisEmprestados() {
        return livroRepository.findLivrosMaisEmprestados().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LivroDTO> findLivrosDisponiveis() {
        return livroRepository.findLivrosDisponiveis().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LivroDTO> findByAnoPublicacaoBetween(Integer anoInicio, Integer anoFim) {
        return livroRepository.findByAnoPublicacaoBetween(anoInicio, anoFim).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LivroDTO> findRandomLivros(Integer limit) {
        return livroRepository.findRandomLivros(limit).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public LivroDTO save(LivroDTO livroDTO) {
        Livro livro = convertToEntity(livroDTO);
        Livro savedLivro = livroRepository.save(livro);
        return convertToDto(savedLivro);
    }

    @Transactional
    public LivroDTO update(Integer id, LivroDTO livroDTO) {
        Livro existingLivro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com id: " + id));

        existingLivro.setTitulo(livroDTO.getTitulo());
        existingLivro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        existingLivro.setStatus(livroDTO.getStatus());

        // Atualizar autores se forem fornecidos
        if (livroDTO.getAutores() != null && !livroDTO.getAutores().isEmpty()) {
            Set<Autor> autores = new HashSet<>();
            for (var autorDTO : livroDTO.getAutores()) {
                Autor autor = autorRepository.findById(autorDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id: " + autorDTO.getId()));
                autores.add(autor);
            }
            existingLivro.setAutores(autores);
        }

        Livro updatedLivro = livroRepository.save(existingLivro);
        return convertToDto(updatedLivro);
    }

    @Transactional
    public void delete(Integer id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com id: " + id));

        livroRepository.delete(livro);
    }

    // Métodos de conversão entre Entity e DTO
    private LivroDTO convertToDto(Livro livro) {
        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setId(livro.getId());
        livroDTO.setTitulo(livro.getTitulo());
        livroDTO.setAnoPublicacao(livro.getAnoPublicacao());
        livroDTO.setStatus(livro.getStatus());

        // Converter autores
        if (livro.getAutores() != null) {
            livroDTO.setAutores(livro.getAutores().stream()
                    .map(autor -> autorService.findById(autor.getId()))
                    .collect(Collectors.toSet()));
        }

        return livroDTO;
    }

    private Livro convertToEntity(LivroDTO livroDTO) {
        Livro livro = new Livro();

        if (livroDTO.getId() != null) {
            livro.setId(livroDTO.getId());
        }

        livro.setTitulo(livroDTO.getTitulo());
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setStatus(livroDTO.getStatus());

        // Converter autores
        if (livroDTO.getAutores() != null) {
            Set<Autor> autores = new HashSet<>();
            for (var autorDTO : livroDTO.getAutores()) {
                Autor autor = autorRepository.findById(autorDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id: " + autorDTO.getId()));
                autores.add(autor);
            }
            livro.setAutores(autores);
        }

        return livro;
    }
}

