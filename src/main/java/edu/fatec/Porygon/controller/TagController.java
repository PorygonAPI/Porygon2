package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.TagRepository;
import edu.fatec.Porygon.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("tag", new Tag());
        model.addAttribute("tags", tagService.listarTagsOrdenadas());
        return "tag";
    }

    @PostMapping("/salvar")
    public String salvarTag(Tag tag, RedirectAttributes redirectAttributes) {
        try {
            tagService.criarTag(tag);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tag cadastrada com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/tags";
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Tag>> buscarTags(@RequestParam String nome) {
        List<Tag> tags = tagRepository.findByNomeStartingWith(nome); // Método de instância
        return ResponseEntity.ok(tags);
    }

}