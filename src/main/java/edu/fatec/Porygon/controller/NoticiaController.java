package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticiaController {

    @Autowired
    private NoticiaRepository noticiaRepository; // camelCase

    @GetMapping("/index")
    public String listarNoticias(Model model) {
        List<Noticia> noticias = noticiaRepository.findAll();
        model.addAttribute("noticias", noticias);
        return "index";
    }

}


