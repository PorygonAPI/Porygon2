package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Api;
import edu.fatec.Porygon.repository.AgendadorRepository;
import edu.fatec.Porygon.repository.TagRepository;
import edu.fatec.Porygon.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/apis")
public class ApiController {

    @Autowired
    private AgendadorRepository agendadorRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ApiService apiService;

    @GetMapping()
    public String mostrarFormularioCadastro(Model model) {
        Api novaApi = new Api();
        novaApi.setAtivo(true); 
        model.addAttribute("api", novaApi);
        model.addAttribute("apis", apiService.listarTodas());
        model.addAttribute("agendadores", agendadorRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
        return "api";
    }

    @PostMapping("/salvar")
    public String salvarOuAtualizarApi(@ModelAttribute Api api) {
        apiService.salvarOuAtualizar(api);
        return "redirect:/apis";
    }
}