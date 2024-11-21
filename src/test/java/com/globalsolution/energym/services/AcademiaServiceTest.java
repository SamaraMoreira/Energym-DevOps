package com.globalsolution.energym.services;

import com.globalsolution.energym.domain.entities.Academia;
import com.globalsolution.energym.domain.entities.Endereco;
import com.globalsolution.energym.dto.AcademiaCreateDTO;
import com.globalsolution.energym.dto.EnderecoCreateDTO;
import com.globalsolution.energym.repositories.AcademiaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademiaServiceTest {

    @InjectMocks
    private AcademiaService service;

    @Mock
    private AcademiaRepository repository;

    @Mock
    private EnderecoService enderecoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @DisplayName("Deve lançar exception ao tentar buscar academia por ID inexistente")
    @Test
    void deveLancarExceptionAoBuscarAcademiaInexistente() {
        Long academiaId = 1L;
        when(repository.findById(academiaId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(academiaId));
    }

    @DisplayName("Deve salvar nova academia com sucesso")
    @Test
    void deveSalvarNovaAcademia() {
        AcademiaCreateDTO academiaCreateDTO = new AcademiaCreateDTO();
        academiaCreateDTO.setCnpj("12345678901234");
        academiaCreateDTO.setNome("Academia Teste");
        EnderecoCreateDTO enderecoCreateDTO = new EnderecoCreateDTO();
        academiaCreateDTO.setEndereco(enderecoCreateDTO);

        Endereco endereco = new Endereco(null, "123", "Rua A", "Bairro B", "Cidade C", "Estado E", "12345678");
        when(enderecoService.save(any(EnderecoCreateDTO.class))).thenReturn(endereco);
        when(repository.save(any(Academia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.saveNewAcademia(academiaCreateDTO);

        verify(repository).save(any(Academia.class));
        verify(enderecoService).save(any(EnderecoCreateDTO.class));
    }


    @DisplayName("Deve lançar exception ao atualizar academia inexistente")
    @Test
    void deveLancarExceptionAoAtualizarAcademiaInexistente() {
        Long academiaId = 1L;
        AcademiaCreateDTO academiaCreateDTO = new AcademiaCreateDTO();
        when(repository.findById(academiaId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(academiaCreateDTO, academiaId));
    }

}
