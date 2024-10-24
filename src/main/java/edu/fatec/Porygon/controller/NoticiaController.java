package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
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

    @GetMapping("/")
    public String listarNoticias(Model model) {
        List<Noticia> noticias = noticiaRepository.findAll();
        noticias.sort(Comparator.comparing(Noticia::getTitulo));
        model.addAttribute("noticias", noticias);
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
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        if (dataFim.isBefore(dataInicio)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data final não pode ser anterior à data inicial.");
        }

        List<Noticia> noticias = noticiaService.listarNoticiasPorData(dataInicio, dataFim);
        noticias.sort(Comparator.comparing(Noticia::getData));
        return ResponseEntity.ok(noticias);
    }
}
