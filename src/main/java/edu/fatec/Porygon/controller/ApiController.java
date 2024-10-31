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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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

    @GetMapping("/editar/{id}")
    public String editarApi(@PathVariable Integer id, Model model) {
        Api api = apiService.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("api", api);
        model.addAttribute("apis", apiService.listarTodas());
        model.addAttribute("agendadores", agendadorRepository.findAll());
        model.addAttribute("formatos", formatoRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
        return "api";
    }

    @PostMapping("/salvar")
    public String salvarOuAtualizarApi(@ModelAttribute Api api,
                                       @RequestParam(value = "tagIds", required = false) List<Integer> tagIds,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        try {
            // Verifica se os IDs de tags foram fornecidos; caso contrário, passa uma lista vazia
            if (tagIds == null) {
                tagIds = List.of();
            }

            // Chama o método com o segundo parâmetro (tagIds)
            String mensagemSucesso = apiService.salvarOuAtualizar(api, tagIds);
            redirectAttributes.addFlashAttribute("mensagemSucesso", mensagemSucesso);
            return "redirect:/apis";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erro", ex.getMessage());
            model.addAttribute("api", api);
            model.addAttribute("apis", apiService.listarTodas());
            model.addAttribute("agendadores", agendadorRepository.findAll());
            model.addAttribute("formatos", formatoRepository.findAll());
            model.addAttribute("tags", tagRepository.findAll());
            return "api";
        } catch (RuntimeException ex) {
            model.addAttribute("erro", " " + ex.getMessage());
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