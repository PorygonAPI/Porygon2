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

    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("portal", new Portal());
        model.addAttribute("agendadores", agendadorRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());  
        return "portal";
    }   

    @PostMapping("/salvar")
    public String salvarPortal(@ModelAttribute Portal portal) {
        portalRepository.save(portal);
        return "redirect:/portais/listar";
    }

    @GetMapping("/listar")
    public String listarPortais(Model model) {
        model.addAttribute("portais", portalRepository.findAll());
        return "listar-portais"; // Retorna o template listar-portais.html
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        Portal portal = portalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("portal", portal);
        model.addAttribute("agendadores", agendadorRepository.findAll());
        return "portal"; // Retorna o template portal.html
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarPortal(@PathVariable("id") Integer id, @ModelAttribute Portal portal) {
        portal.setId(id);
        portalRepository.save(portal);
        return "redirect:/portais/listar";
    }

    @GetMapping("/alternar/{id}")
    public String alternarAtivo(@PathVariable("id") Integer id) {
        Portal portal = portalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        portal.setAtivo(!portal.isAtivo());
        portalRepository.save(portal);
        return "redirect:/portais/listar";
    }
}
