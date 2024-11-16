package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.dto.NoticiaDTO;
import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
        List<Noticia> noticias = noticiaRepository.findAll();
        noticias.sort(Comparator.comparing(Noticia::getId).reversed());
        model.addAttribute("noticias", noticias);
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
            @RequestParam(value = "tagIds", required = false) List<Integer> tagIds) {

        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data final não pode ser anterior à data inicial.");
        }

        List<Noticia> noticias = noticiaService.listarNoticiasPorFiltros(dataInicio, dataFim, tagIds);

        if (noticias.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("mensagem", "Nenhuma notícia encontrada"));
        }

        List<NoticiaDTO> noticiaDTOs = noticias.stream()
                .map(NoticiaDTO::new)
                .sorted(Comparator.comparing(NoticiaDTO::getData).reversed())
                .collect(Collectors.toList());

        return ResponseEntity.ok(noticiaDTOs);
    }
}
