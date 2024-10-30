package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Portal;
import edu.fatec.Porygon.repository.AgendadorRepository;
import edu.fatec.Porygon.repository.PortalRepository;
import edu.fatec.Porygon.repository.TagRepository;
import edu.fatec.Porygon.service.DataScrapperService;
import edu.fatec.Porygon.service.PortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/portais")
public class PortalController {

    @Autowired
    private PortalRepository portalRepository;

    @Autowired
    private AgendadorRepository agendadorRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PortalService portalService;

    @Autowired
    private DataScrapperService dataScrapperService;

    @GetMapping()
    public String mostrarFormularioCadastro(Model model) {
        Portal novoPortal = new Portal();
        novoPortal.setAtivo(true);
        model.addAttribute("portal", novoPortal);
        model.addAttribute("portais", portalRepository.findAll());
        model.addAttribute("agendadores", agendadorRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
        return "portal";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        Portal portal = portalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        model.addAttribute("portal", portal);
        model.addAttribute("portais", portalRepository.findAll());
        model.addAttribute("agendadores", agendadorRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
        return "portal";
    }

    @GetMapping("/verificarNome")
    @ResponseBody
    public Map<String, Boolean> verificarNome(@RequestParam String nome) {
        boolean existe = portalRepository.existsByNome(nome);
        return Map.of("existe", existe);
    }

    @PostMapping("/salvar")
    public String salvarOuAtualizarPortal(@ModelAttribute Portal portal, @RequestParam("isEdit") boolean isEdit,
            Model model) {
        String errorMessage = null;
        String successMessage = null;

        if (isEdit) {
            Portal portalExistente = portalRepository.findById(portal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + portal.getId()));

            if (!portal.getNome().equals(portalExistente.getNome())
                    && portalRepository.existsByNome(portal.getNome())) {
                errorMessage = "Já existe um portal com esse nome.";
            } else if (!portal.getUrl().equals(portalExistente.getUrl())
                    && portalRepository.existsByUrl(portal.getUrl())) {
                errorMessage = "Já existe um portal com essa URL.";
            }
            portal.setUltimaAtualizacao(portalExistente.getUltimaAtualizacao());
            portal.setDataCriacao(portalExistente.getDataCriacao());
        } else {
            if (portalRepository.existsByNome(portal.getNome())) {
                errorMessage = "Já existe um portal com este nome.";
            } else if (portalRepository.existsByUrl(portal.getUrl())) {
                errorMessage = "Já existe um portal com esta URL.";
            }
        }

        if (errorMessage != null) {
            model.addAttribute("portal", portal);
            model.addAttribute("portais", portalRepository.findAll());
            model.addAttribute("agendadores", agendadorRepository.findAll());
            model.addAttribute("tags", tagRepository.findAll());
            model.addAttribute("errorMessage", errorMessage);
            return "portal";
        }

        if (portal.getId() == null) {
            portal.setDataCriacao(LocalDate.now());
        } else {
            Portal portalExistente = portalRepository.findById(portal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + portal.getId()));
            portal.setDataCriacao(portalExistente.getDataCriacao());
        }

        portalRepository.save(portal);

        if (!isEdit && portal.isAtivo() && !portal.isHasScrapedToday()) {
            dataScrapperService.WebScrapper();
            portal.setHasScrapedToday(true);
            portal.setUltimaAtualizacao(LocalDate.now());
            portalRepository.save(portal);
            successMessage = "Cadastro de portal e primeira coleta realizada com sucesso!";
        } else if (!isEdit && !portal.isAtivo()) {
            successMessage = "Cadastro de portal realizado sem coleta por estar desativo.";
        } else {
            successMessage = "Portal editado com sucesso!";
        }

        model.addAttribute("successMessage", successMessage);
        model.addAttribute("portal", portal);
        model.addAttribute("portais", portalRepository.findAll());
        model.addAttribute("agendadores", agendadorRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());

        return "redirect:/portais?successMessage=" + successMessage;
    }

    @PostMapping("/alterarStatus/{id}")
    public ResponseEntity<String> alterarStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> body) {
        boolean novoStatus = body.get("ativo");
        Portal portal = portalService.alterarStatus(id, novoStatus);

        String message;
        if (novoStatus && portal != null && !portal.isHasScrapedToday()) {
            dataScrapperService.WebScrapper();
            message = "Portal ativado e raspagem realizada com sucesso.";
        } else if (novoStatus) {
            message = "Portal ativado e raspagem realizada com sucesso.";
        } else {
            message = "Portal desativado com sucesso.";
        }
        return ResponseEntity.ok(message);
    }

}
