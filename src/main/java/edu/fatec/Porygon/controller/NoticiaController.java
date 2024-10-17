package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NoticiaController {

    @Autowired
    private NoticiaRepository noticiaRepository;

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
        
        if (noticiaOptional.isPresent()) {
            Noticia noticia = noticiaOptional.get();
            if (noticia.getJornalista() != null) {
                Hibernate.initialize(noticia.getJornalista());
            }
            return ResponseEntity.ok(noticia);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}