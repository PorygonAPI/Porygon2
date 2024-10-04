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

    @PostMapping("/salvar")
    public String salvarOuAtualizarPortal(@ModelAttribute Portal portal, @RequestParam("isEdit") boolean isEdit) {
        if (portal.getId() == null) {
            portal.setDataCriacao(LocalDate.now());
        } else {
            Portal portalExistente = portalRepository.findById(portal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + portal.getId()));
            portal.setDataCriacao(portalExistente.getDataCriacao());
        }
        portalRepository.save(portal);

        if (!isEdit) {
            dataScrapperService.WebScrapper();
        }

        return "redirect:/portais";
    }

    @PostMapping("/alterarStatus/{id}")
    public ResponseEntity<?> alterarStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> body) {
        boolean novoStatus = body.get("ativo");
        portalService.alterarStatus(id, novoStatus); 
        return ResponseEntity.ok().build();
    }
}
