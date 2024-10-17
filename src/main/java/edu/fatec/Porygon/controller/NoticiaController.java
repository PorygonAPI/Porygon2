package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
// import edu.fatec.Porygon.service.NoticiaService;

import java.util.List;
import java.util.Optional;

// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

// import ch.qos.logback.classic.Logger;

@Controller
public class NoticiaController {

    @Autowired
    private NoticiaRepository noticiaRepository;
    // @Autowired
    // private NoticiaService noticiaService;

    // private static final Logger logger = (Logger) LoggerFactory.getLogger(NoticiaController.class);

    @GetMapping("/index")
    public String listarNoticias(Model model) {
        List<Noticia> noticias = noticiaRepository.findAll();
        model.addAttribute("noticias", noticias);
        return "index";
    }
    @GetMapping("/noticias/detalhe/{id}")
    @ResponseBody
    public ResponseEntity<Noticia> detalheNoticiaJson(@PathVariable Integer id) {
        Optional<Noticia> noticiaOptional = noticiaRepository.findById(id);
        return noticiaOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/validarIntervalo")
    public ResponseEntity<String> validarIntervalo(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date dataFim) {

        if (dataFim.before(dataInicio)) {
            return ResponseEntity.badRequest().body("A data final não pode ser anterior à data inicial.");
        }

        // Lógica adicional para processar o intervalo válido
        return ResponseEntity.ok("Intervalo de datas válido.");
    }

}


