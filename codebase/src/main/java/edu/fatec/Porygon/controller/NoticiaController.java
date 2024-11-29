package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.dto.NoticiaDTO;
import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.fatec.Porygon.repository.TagRepository;
import edu.fatec.Porygon.service.NoticiaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
public class NoticiaController {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private NoticiaService noticiaService;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/")
    public String listarNoticias(Model model) {
        model.addAttribute("tags", tagRepository.findAll());
        return "index";
    }

    @GetMapping("/noticias/{id}")
    @ResponseBody
    public ResponseEntity<NoticiaDTO> getNoticiaDetails(@PathVariable Integer id) {
        Optional<Noticia> noticiaOpt = noticiaRepository.findById(id);
        if (noticiaOpt.isPresent()) {
            NoticiaDTO noticiaDTO = new NoticiaDTO(noticiaOpt.get());
            return ResponseEntity.ok(noticiaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/noticias")
    @ResponseBody
    public ResponseEntity<?> listarNoticias(
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(value = "tagIds", required = false) List<Integer> tagIds,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data final não pode ser anterior à data inicial.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Noticia> noticiasPage = noticiaService.listarNoticiasPorFiltros(dataInicio, dataFim, tagIds, pageable);

        if (noticiasPage.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("mensagem", "Nenhuma notícia encontrada"));
        }

        List<NoticiaDTO> noticiaDTOs = noticiasPage.getContent().stream()
                .map(NoticiaDTO::new)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("content", noticiaDTOs);
        response.put("totalPages", noticiasPage.getTotalPages());

        return ResponseEntity.ok(response);
    }
}
