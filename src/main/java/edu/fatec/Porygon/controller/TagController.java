package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("tag", new Tag());
        model.addAttribute("tags", tagService.listarTagsOrdenadas()); // Certifique-se de que este método está disponível
        return "tag";
    }

    @PostMapping("/salvar")
    public String salvarTag(@ModelAttribute Tag tag, RedirectAttributes redirectAttributes) {
        try {
            if (tag.getId() != null) {
                tagService.editarTag(tag.getId(), tag.getNome());
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Tag atualizada com sucesso!");
            } else {
                tagService.criarTag(tag); // Isso agora verifica duplicidade
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Tag cadastrada com sucesso!");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/tags";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Integer id, Model model) {
        Tag tag = tagService.buscarTagPorId(id); // Método para buscar a tag pelo ID
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tagService.listarTagsOrdenadas());
        return "tag"; // Retorna para a mesma página, mas agora com a tag a ser editada
    }
}
