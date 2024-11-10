package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.dto.NoticiaDTO;
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

    @GetMapping("/associar-tags")
    @ResponseBody
    public ResponseEntity<String> associarTagsANoticias() {
        try {
            noticiaService.findTagsInTitle();
            noticiaService.associarTagsPorConteudo();
            return ResponseEntity.ok("Tags associadas com sucesso às notícias.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao associar tags às notícias: " + e.getMessage());
        }
    }

}
