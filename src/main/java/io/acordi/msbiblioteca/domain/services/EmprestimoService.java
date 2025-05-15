package io.acordi.msbiblioteca.domain.services;

import io.acordi.msbiblioteca.entrypoint.dto.EmprestimoDTO;
import io.acordi.msbiblioteca.domain.exception.ResourceNotFoundException;
import io.acordi.msbiblioteca.domain.entities.Emprestimo;
import io.acordi.msbiblioteca.domain.entities.Livro;
import io.acordi.msbiblioteca.domain.entities.Membro;
import io.acordi.msbiblioteca.infra.repository.EmprestimoRepository;
import io.acordi.msbiblioteca.infra.repository.LivroRepository;
import io.acordi.msbiblioteca.infra.repository.MembroRepository;
import io.acordi.msbiblioteca.domain.types.StatusLivro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private MembroRepository membroRepository;

    public List<EmprestimoDTO> findAll() {
        return emprestimoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EmprestimoDTO findById(Integer id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com id: " + id));
        return convertToDto(emprestimo);
    }

    public List<EmprestimoDTO> findByMembro(Integer membroId) {
        return emprestimoRepository.findByMembroId(membroId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmprestimoDTO> findByLivro(Integer livroId) {
        return emprestimoRepository.findByLivroId(livroId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmprestimoDTO> findEmprestimosAtivos() {
        return emprestimoRepository.findEmprestimosAtivos().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmprestimoDTO> findEmprestimosAtrasados() {
        LocalDate hoje = LocalDate.now();
        return emprestimoRepository.findEmprestimosAtrasados(hoje).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmprestimoDTO> findByDataDevolucaoBetween(LocalDate dataInicio, LocalDate dataFim) {
        return emprestimoRepository.findByDataDevolucaoBetween(dataInicio, dataFim).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmprestimoDTO> findEmprestimosAtivosByMembro(Integer membroId) {
        return emprestimoRepository.findEmprestimosAtivosByMembro(membroId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Integer countEmprestimosAtivosByMembro(Integer membroId) {
        return emprestimoRepository.countEmprestimosAtivosByMembro(membroId);
    }

    public List<EmprestimoDTO> findEmprestimosHistoricoByLivro(Integer livroId) {
        return emprestimoRepository.findEmprestimosHistoricoByLivro(livroId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmprestimoDTO save(EmprestimoDTO emprestimoDTO) {
        // Verificar se o membro existe
        Membro membro = membroRepository.findById(emprestimoDTO.getMembroId())
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com id: " + emprestimoDTO.getMembroId()));

        // Verificar se o livro existe
        Livro livro = livroRepository.findById(emprestimoDTO.getLivroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado com id: " + emprestimoDTO.getLivroId()));

        // Verificar se o livro está disponível para empréstimo
        if (livro.getStatus() == StatusLivro.EMPRESTADO) {
            throw new IllegalStateException("Livro já está emprestado");
        }

        // Verificar limite de empréstimos do membro
        Integer emprestimosAtivos = emprestimoRepository.countEmprestimosAtivosByMembro(membro.getId());
        if (emprestimosAtivos >= 3) {
            throw new IllegalStateException("Membro já atingiu o limite de 3 empréstimos ativos");
        }

        Emprestimo emprestimo = convertToEntity(emprestimoDTO);
        emprestimo.setMembro(membro);
        emprestimo.setLivro(livro);

        // O trigger do banco atualizará o status do livro para emprestado

        Emprestimo savedEmprestimo = emprestimoRepository.save(emprestimo);
        return convertToDto(savedEmprestimo);
    }

    @Transactional
    public EmprestimoDTO registrarDevolucao(Integer id, LocalDate dataDevolucao) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com id: " + id));

        if (emprestimo.getDataDevolucao() != null) {
            throw new IllegalStateException("Este empréstimo já foi devolvido");
        }

        emprestimo.setDataDevolucao(dataDevolucao != null ? dataDevolucao : LocalDate.now());

        // O trigger do banco atualizará o status do livro para disponível
        // O trigger do banco também criará uma multa, se necessário

        Emprestimo updatedEmprestimo = emprestimoRepository.save(emprestimo);
        return convertToDto(updatedEmprestimo);
    }

    @Transactional
    public EmprestimoDTO update(Integer id, EmprestimoDTO emprestimoDTO) {
        Emprestimo existingEmprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com id: " + id));

        // Não permitir alterar o livro ou membro de um empréstimo existente
        existingEmprestimo.setDataEmprestimo(emprestimoDTO.getDataEmprestimo());
        existingEmprestimo.setDataDevolucaoPrevista(emprestimoDTO.getDataDevolucaoPrevista());
        existingEmprestimo.setDataDevolucao(emprestimoDTO.getDataDevolucao());

        Emprestimo updatedEmprestimo = emprestimoRepository.save(existingEmprestimo);
        return convertToDto(updatedEmprestimo);
    }

    @Transactional
    public void delete(Integer id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com id: " + id));

        // Verificar se há multas associadas
        if (!emprestimo.getMultas().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir um empréstimo com multas associadas");
        }

        emprestimoRepository.delete(emprestimo);
    }

    // Métodos de conversão entre Entity e DTO
    private EmprestimoDTO convertToDto(Emprestimo emprestimo) {
        EmprestimoDTO emprestimoDTO = new EmprestimoDTO();
        emprestimoDTO.setId(emprestimo.getId());
        emprestimoDTO.setMembroId(emprestimo.getMembro().getId());
        emprestimoDTO.setLivroId(emprestimo.getLivro().getId());
        emprestimoDTO.setDataEmprestimo(emprestimo.getDataEmprestimo());
        emprestimoDTO.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());
        emprestimoDTO.setDataDevolucao(emprestimo.getDataDevolucao());

        // Adicionar informações extras para exibição
        emprestimoDTO.setNomeMembro(emprestimo.getMembro().getNome());
        emprestimoDTO.setTituloLivro(emprestimo.getLivro().getTitulo());

        return emprestimoDTO;
    }

    private Emprestimo convertToEntity(EmprestimoDTO emprestimoDTO) {
        Emprestimo emprestimo = new Emprestimo();

        if (emprestimoDTO.getId() != null) {
            emprestimo.setId(emprestimoDTO.getId());
        }

        emprestimo.setDataEmprestimo(emprestimoDTO.getDataEmprestimo());
        emprestimo.setDataDevolucaoPrevista(emprestimoDTO.getDataDevolucaoPrevista());
        emprestimo.setDataDevolucao(emprestimoDTO.getDataDevolucao());

        return emprestimo;
    }
}
