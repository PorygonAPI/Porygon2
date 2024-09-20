package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Portal;
import edu.fatec.Porygon.repository.AgendadorRepository;
import edu.fatec.Porygon.repository.PortalRepository;
import edu.fatec.Porygon.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/portais")
public class PortalController {

    @Autowired
    private PortalRepository portalRepository;

    @Autowired
    private AgendadorRepository agendadorRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping()
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("portal", new Portal());
        model.addAttribute("portais", portalRepository.findAll()); // listar os portais
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
    public String salvarOuAtualizarPortal(@ModelAttribute Portal portal) {
        if (portal.getId() == null) {
            portal.setdataCriacao(LocalDate.now());
        }
        portalRepository.save(portal);
        return "redirect:/portais";
    }

    // @GetMapping("/alternar/{id}")
    // public String alternarAtivo(@PathVariable("id") Integer id) {
    //     Portal portal = portalRepository.findById(id)
    //         .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

    //     portal.setAtivo(!portal.isAtivo()); 
    //     portalRepository.save(portal);
    //     return "redirect:/portais"; 
    // }
}