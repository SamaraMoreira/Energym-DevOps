package com.globalsolution.energym.services;

import com.globalsolution.energym.domain.entities.Exercicio;
import com.globalsolution.energym.domain.entities.Praticante;
import com.globalsolution.energym.repositories.PraticanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PraticanteService {

    @Autowired
    private PraticanteRepository repository;


    public Praticante buscarPorCpf(String cpf) {
        Optional<Praticante> praticante = repository.findByCpf(cpf);
        if (praticante.isPresent()){
            praticante.get().setPontos(getTotalPontosPraticante(praticante.get()));
        }
        return praticante.orElse(null);
    }

    public Praticante findById(Long praticanteId) {
        Praticante praticante = repository.findById(praticanteId).orElseThrow(() -> new EntityNotFoundException("Praticante não encontrado para o id: "+ praticanteId));
        praticante.setPontos(getTotalPontosPraticante(praticante));
        return praticante;
    }

    @Transactional
    public double getTotalPontosPraticante(Praticante praticante) {
        double pontos = 0.0;
        for (Exercicio exercicio : praticante.getExercicios()) {
            pontos += exercicio.getPontos();
        }
        return pontos;
    }

    public Praticante save(Praticante praticante) {
        return repository.save(praticante);
    }
}
