package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.SinonimoRepository;
import edu.fatec.Porygon.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private SinonimoRepository sinonimoRepository;

    @GetMapping
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("tag", new Tag());
        model.addAttribute("tags", tagService.listarTagsOrdenadas());
        return "tag";
    }

    @PostMapping("/salvar")
    public String salvarTag(@ModelAttribute Tag tag, @RequestParam(required = false) boolean forcarCadastro, RedirectAttributes redirectAttributes) {
        try {
            if (tag.getId() != null) {
                tagService.editarTag(tag.getId(), tag.getNome());
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Tag atualizada com sucesso!");
            } else {
                tagService.criarTag(tag, forcarCadastro);
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

    @GetMapping("/verificar-sinonimo")
    @ResponseBody
    public Map<String, Boolean> verificarSinonimo(@RequestParam String nome) {
        List<Sinonimo> sinonimosExistentes = sinonimoRepository.findByNome(nome.toLowerCase());
        Map<String, Boolean> response = new HashMap<>();
        response.put("sinonimoEncontrado", !sinonimosExistentes.isEmpty());
        return response;
    }
}