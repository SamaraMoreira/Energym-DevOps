package com.globalsolution.energym.services;

import com.globalsolution.energym.domain.entities.Endereco;
import com.globalsolution.energym.dto.EnderecoCreateDTO;
import com.globalsolution.energym.repositories.EnderecoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {

    @InjectMocks
    private EnderecoService service;

    @Mock
    private EnderecoRepository repository;

    @DisplayName("Deve salvar um endereço com sucesso usando DTO")
    @Test
    void deveSalvarEnderecoComSucessoUsandoDTO() {
        // Arrange
        EnderecoCreateDTO enderecoCreateDTO = new EnderecoCreateDTO("123", "Rua A", "Bairro B", "Cidade C", "Estado E", "12345678");
        Endereco enderecoEsperado = new Endereco(null, "123", "Rua A", "Bairro B", "Cidade C", "Estado E", "12345678");

        when(repository.save(any(Endereco.class))).thenReturn(enderecoEsperado);

        // Act
        Endereco resultado = service.save(enderecoCreateDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("123", resultado.getNumero());
        assertEquals("Rua A", resultado.getRua());
        assertEquals("Bairro B", resultado.getBairro());
        assertEquals("Cidade C", resultado.getCidade());
        assertEquals("Estado E", resultado.getEstado());
        assertEquals("12345678", resultado.getCep());

        verify(repository, times(1)).save(any(Endereco.class));
    }

    @DisplayName("Deve salvar um endereço com sucesso usando entidade Endereco")
    @Test
    void deveSalvarEnderecoComSucessoUsandoEntidade() {
        // Arrange
        Endereco endereco = new Endereco(null, "123", "Rua A", "Bairro B", "Cidade C", "Estado E", "12345678");

        when(repository.save(any(Endereco.class))).thenReturn(endereco);

        // Act
        Endereco resultado = service.save(endereco);

        // Assert
        assertNotNull(resultado);
        assertEquals("123", resultado.getNumero());
        assertEquals("Rua A", resultado.getRua());
        assertEquals("Bairro B", resultado.getBairro());
        assertEquals("Cidade C", resultado.getCidade());
        assertEquals("Estado E", resultado.getEstado());
        assertEquals("12345678", resultado.getCep());

        verify(repository, times(1)).save(any(Endereco.class));
    }

}
