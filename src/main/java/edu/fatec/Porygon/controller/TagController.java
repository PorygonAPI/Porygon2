package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

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
    public String salvarTag(@ModelAttribute Tag tag, @RequestParam("confirmacaoCadastro") boolean confirmacaoCadastro, RedirectAttributes redirectAttributes) {
        try {
            if (!confirmacaoCadastro) {
                Optional<Tag> tagExistente = tagService.verificarSinonimo(tag.getNome());
                if (tagExistente.isPresent()) {
                    redirectAttributes.addFlashAttribute("mensagemErro", "Já existe um sinônimo cadastrado: " + tagExistente.get().getNome());
                    redirectAttributes.addFlashAttribute("confirmarCadastro", true);
                    redirectAttributes.addFlashAttribute("tag", tag);
                    return "redirect:/tags";
                }
            }

            if (tag.getId() != null) {
                tagService.editarTag(tag.getId(), tag.getNome());
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Tag atualizada com sucesso!");
            } else {
                tagService.criarTag(tag, confirmacaoCadastro);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Tag cadastrada com sucesso!");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/tags";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Integer id, Model model) {
        Tag tag = tagService.buscarTagPorId(id);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tagService.listarTagsOrdenadas());
        return "tag";
    }
}
