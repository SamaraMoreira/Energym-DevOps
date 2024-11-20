package com.globalsolution.energym.services;

import com.globalsolution.energym.domain.entities.Praticante;
import com.globalsolution.energym.repositories.PraticanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PraticanteService {

    @Autowired
    private PraticanteRepository repository;


    public Praticante buscarPorCpf(String cpf) {
        Optional<Praticante> praticante = repository.findByCpf(cpf);
        return praticante.orElse(null);
    }

    public Praticante findById(Long praticanteId) {
        return repository.findById(praticanteId).orElseThrow(() -> new EntityNotFoundException("Praticante não encontrado para o id: "+ praticanteId));
    }
}