package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.TagRepository;
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
    private TagRepository tagRepository;

    @GetMapping
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("tag", new Tag());
        return "tag";
    }

    @PostMapping("/salvar")
    public String salvarTag(Tag tag, RedirectAttributes redirectAttributes) {
        if (tagRepository.findByNome(tag.getNome()).isPresent()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Já existe uma tag com esse nome.");
            return "redirect:/tags";
        }
        tagRepository.save(tag);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Tag cadastrada com sucesso!");
        return "redirect:/tags";
    }
}