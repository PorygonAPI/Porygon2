package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
import edu.fatec.Porygon.service.NoticiaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class NoticiaController {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private NoticiaService noticiaService;

@GetMapping("/")
    public String listarNoticias(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("titulo").ascending());
        Page<Noticia> noticiaPage = noticiaService.listarNoticias(pageable);
        
        model.addAttribute("noticias", noticiaPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", noticiaPage.getTotalPages());
        model.addAttribute("totalItems", noticiaPage.getTotalElements());
        
        return "index";
    }

    @GetMapping("/noticias/detalhe/{id}")
    @ResponseBody
    public ResponseEntity<Noticia> detalheNoticiaJson(@PathVariable Integer id) {
        Optional<Noticia> noticiaOptional = noticiaRepository.findById(id);
        return noticiaOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/noticias")
    @ResponseBody
    public ResponseEntity<?> listarNoticiasPorData(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (dataFim.isBefore(dataInicio)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data final não pode ser anterior à data inicial.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("data").ascending());
        Page<Noticia> noticiaPage = noticiaService.listarNoticiasPorData(dataInicio, dataFim, pageable);
        return ResponseEntity.ok(noticiaPage);
    }
}
