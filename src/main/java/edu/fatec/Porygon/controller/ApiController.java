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

import java.time.LocalDate;
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
    public String mostrarFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        Api api = apiService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        model.addAttribute("api", api);
        model.addAttribute("apis", apiService.listarTodas());
        model.addAttribute("agendadores", agendadorRepository.findAll());
        model.addAttribute("formatos", formatoRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
        return "api";
    }

    @PostMapping("/salvar")
    public String salvarOuAtualizarApi(@ModelAttribute Api api, @RequestParam("isEdit") boolean isEdit, Model model) {
        String errorMessage = null;

        if (isEdit) {
            Api apiExistente = apiService.buscarPorId(api.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + api.getId()));

            if (!api.getNome().equals(apiExistente.getNome()) && apiService.existsByNome(api.getNome())) {
                errorMessage = "Já existe uma API com esse nome.";
            } else if (!api.getUrl().equals(apiExistente.getUrl()) && apiService.existsByUrl(api.getUrl())) {
                errorMessage = "Já existe uma API com essa URL.";
            }
        } else {
            if (apiService.existsByNome(api.getNome())) {
                errorMessage = "Já existe uma API com este nome.";
            } else if (apiService.existsByUrl(api.getUrl())) {
                errorMessage = "Já existe uma API com esta URL.";
            }
        }

        if (errorMessage != null) {
            model.addAttribute("erro", errorMessage);
            model.addAttribute("api", api);
            model.addAttribute("apis", apiService.listarTodas());
            model.addAttribute("agendadores", agendadorRepository.findAll());
            model.addAttribute("formatos", formatoRepository.findAll());
            model.addAttribute("tags", tagRepository.findAll());
            return "api";
        }

        if (api.getId() == null) {
            api.setDataCriacao(LocalDate.now());
        } else {
            Api apiExistente = apiService.buscarPorId(api.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + api.getId()));
            api.setDataCriacao(apiExistente.getDataCriacao());
            api.setUltimaAtualizacao(apiExistente.getUltimaAtualizacao());
        }
        apiService.salvarOuAtualizar(api);
        return "redirect:/apis";
    }
    

    @PostMapping("/alterarStatus/{id}")
    public ResponseEntity<?> alterarStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> body) {
        boolean novoStatus = body.get("ativo");
        apiService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok().build();
    }
}
