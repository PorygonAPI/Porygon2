package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Api;
import edu.fatec.Porygon.repository.AgendadorRepository;
import edu.fatec.Porygon.repository.FormatoRepository;
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

    @Autowired
    private FormatoRepository formatoRepository;

    @GetMapping()
    public String mostrarFormularioCadastro(Model model) {
        Api novaApi = new Api();
        novaApi.setAtivo(true); 
        model.addAttribute("api", novaApi);
        model.addAttribute("apis", apiService.listarTodas());
        model.addAttribute("agendadores", agendadorRepository.findAll());
        model.addAttribute("formatos", formatoRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
        return "api";
    }

    @PostMapping("/salvar")
    public String salvarOuAtualizarApi(@ModelAttribute Api api, Model model) {
        try {
            apiService.salvarOuAtualizar(api);
            return "redirect:/apis";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            model.addAttribute("api", api);
            model.addAttribute("apis", apiService.listarTodas());
            model.addAttribute("agendadores", agendadorRepository.findAll());
            model.addAttribute("formatos", formatoRepository.findAll());
            model.addAttribute("tags", tagRepository.findAll());
            return "api";
        }
    }
    
    @PostMapping("/alterarStatus/{id}")
    public ResponseEntity<?> alterarStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> body) {
        boolean novoStatus = body.get("ativo");
        apiService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok().build();
    }
}
