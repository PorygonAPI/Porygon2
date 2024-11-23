package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Portal;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.AgendadorRepository;
import edu.fatec.Porygon.repository.PortalRepository;
import edu.fatec.Porygon.repository.TagRepository;
import edu.fatec.Porygon.service.DataScrapperService;
import edu.fatec.Porygon.service.PortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Portal portal = new Portal(); 
        portal.setAtivo(true);
        carregarModelBase(model, portal);
        return "portal";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        Portal portal = portalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        carregarModelBase(model, portal);
        return "portal";
    }

    @PostMapping("/salvar")
    public String salvarOuAtualizarPortal(
            @ModelAttribute Portal portal,
            @RequestParam("isEdit") boolean isEdit,
            @RequestParam(required = false) String tagIds,
            Model model) {

        String errorMessage = null;
        String successMessage = null;

        List<Integer> tagIdsList = processarTags(tagIds);
        if (tagIdsList != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIdsList));
            portal.setTags(tags);
        }

        try {
            dataScrapperService.validarSeletores(portal);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        } catch (RuntimeException e) {
            errorMessage = "Erro ao acessar a URL: " + e.getMessage();
        }

        if (errorMessage != null) {
            carregarModelBase(model, portal);
            model.addAttribute("errorMessage", errorMessage);
            return "portal";
        }

        if (isEdit) {
            errorMessage = verificarDuplicidadeParaEdicao(portal);
        } else {
            errorMessage = verificarDuplicidadeParaCadastro(portal);
        }

        if (errorMessage != null) {
            carregarModelBase(model, portal);
            model.addAttribute("errorMessage", errorMessage);
            return "portal";
        }

        ajustarDatasCadastroEdicao(portal, isEdit);
        portalRepository.save(portal);

        try {
            if (!isEdit && portal.isAtivo() && !portal.isHasScrapedToday()) {
                dataScrapperService.WebScrapper();
                portal.setHasScrapedToday(true);
                portal.setUltimaAtualizacao(LocalDate.now());
            }
        } catch (Exception e) {
            errorMessage = "Erro ao realizar a raspagem: " + e.getMessage();
            carregarModelBase(model, portal);
            model.addAttribute("errorMessage", errorMessage);
            return "portal";
        }

        successMessage = "Portal salvo com sucesso!";
        model.addAttribute("successMessage", successMessage);
        return "redirect:/portais";
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

    private void carregarModelBase(Model model, Portal portal) {
        model.addAttribute("portal", portal);
        model.addAttribute("portais", portalRepository.findAll());
        model.addAttribute("agendadores", agendadorRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
        model.addAttribute("tagsSelecionadas", portal.getTags().stream().map(Tag::getId).collect(Collectors.toList()));
        model.addAttribute("seletoresTitulo", portalService.getSeletoresTitulo());
        model.addAttribute("seletoresJornalista", portalService.getSeletoresJornalista());
        model.addAttribute("seletoresConteudo", portalService.getSeletoresConteudo());
        model.addAttribute("seletoresDataPublicacao", portalService.getSeletoresDataPublicacao());
        model.addAttribute("seletoresCaminhoNoticia", portalService.getSeletoresCaminhoNoticia());
    }

    private List<Integer> processarTags(String tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            return Arrays.stream(tagIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        return null;
    }

    private String verificarDuplicidadeParaEdicao(Portal portal) {
        Portal portalExistente = portalRepository.findById(portal.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + portal.getId()));

        if (!portal.getNome().equals(portalExistente.getNome())
                && portalRepository.existsByNome(portal.getNome())) {
            return "Já existe um portal com esse nome.";
        } else if (!portal.getUrl().equals(portalExistente.getUrl())
                && portalRepository.existsByUrl(portal.getUrl())) {
            return "Já existe um portal com essa URL.";
        }
        return null;
    }

    private String verificarDuplicidadeParaCadastro(Portal portal) {
        if (portalRepository.existsByNome(portal.getNome())) {
            return "Já existe um portal com este nome.";
        } else if (portalRepository.existsByUrl(portal.getUrl())) {
            return "Já existe um portal com esta URL.";
        }
        return null;
    }

    private void ajustarDatasCadastroEdicao(Portal portal, boolean isEdit) {
        if (isEdit) {
            Portal portalExistente = portalRepository.findById(portal.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + portal.getId()));
            portal.setDataCriacao(portalExistente.getDataCriacao());
        } else {
            portal.setDataCriacao(LocalDate.now());
        }
    }
}