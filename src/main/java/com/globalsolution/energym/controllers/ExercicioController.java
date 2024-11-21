package com.globalsolution.energym.controllers;

import com.globalsolution.energym.domain.entities.Academia;
import com.globalsolution.energym.domain.entities.Exercicio;
import com.globalsolution.energym.domain.entities.Praticante;
import com.globalsolution.energym.domain.enums.TipoExercicio;
import com.globalsolution.energym.dto.NovoExercicioDTO;
import com.globalsolution.energym.services.AcademiaService;
import com.globalsolution.energym.services.ExercicioService;
import com.globalsolution.energym.services.PraticanteService;
import com.globalsolution.energym.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exercicios")
public class ExercicioController {

    @Autowired
    private ExercicioService service;

    @Autowired
    private PraticanteService praticanteService;

    @Autowired
    private AcademiaService academiaService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listarExercicios(
            @RequestParam(defaultValue = "false") boolean novoRegistro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model, HttpServletRequest httpServletRequest) {
        Page<Exercicio> exerciciosPage = service.getPageExercicios(page, size);
        if (novoRegistro){
            model.addAttribute("successMessage","Exercício cadastrado com sucesso!");
        }
        model.addAttribute("currentPath", httpServletRequest.getRequestURI());
        model.addAttribute("exerciciosPage", exerciciosPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", exerciciosPage.getTotalPages());
        return "exercicios/index";
    }

    @GetMapping("/form-registrar/{cpf}")
    public String getFormRegistrarNovoExercicio(Model model, @PathVariable("cpf") String cpfParticipante, HttpServletRequest httpServletRequest){
        Praticante praticante = praticanteService.buscarPorCpf(cpfParticipante);
        Academia academiaAutenticada = academiaService.findByUserId(userService.authenticated().getId());
        model.addAttribute("currentPath", httpServletRequest.getRequestURI());
        model.addAttribute("praticante",praticante);
        model.addAttribute("novoExercicioDTO",new NovoExercicioDTO(academiaAutenticada.getId(),praticante.getId(),null,0));
        return "exercicios/registrar";
    }

    @PostMapping("/registrar")
    public String registrarExercicio(@ModelAttribute("novoExercicioDTO") @Valid NovoExercicioDTO novoExercicioDTO,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Por favor, corrija os erros abaixo.");
            return "exercicios/registrar";
        }

        try {
            Academia academia = academiaService.findById(novoExercicioDTO.getAcademiaId());
            Praticante praticante = praticanteService.findById(novoExercicioDTO.getPraticanteId());

            Exercicio exercicio = new Exercicio();
            exercicio.setAcademia(academia);
            exercicio.setPraticante(praticante);
            exercicio.setTipo(TipoExercicio.toEnum(novoExercicioDTO.getTipo()));
            exercicio.setKm(novoExercicioDTO.getKm());
            exercicio.setPontos(exercicio.getKm() * (exercicio.getTipo().getCod() == 2 ? 100 : 150));

            service.save(exercicio);
            praticante.setPontos(exercicio.getPontos() + praticante.getPontos());
            praticanteService.save(praticante);

            return "redirect:/exercicios?novoRegistro=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ocorreu um erro ao registrar o exercício.");
            return "exercicios/registrar";
        }
    }

}
