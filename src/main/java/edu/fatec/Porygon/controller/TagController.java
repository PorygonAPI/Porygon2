package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

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
}