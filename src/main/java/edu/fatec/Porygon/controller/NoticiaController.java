package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.repository.NoticiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticiaController { //Lucas

    @Autowired
    private NoticiaRepository NoticiaRepository;

    @GetMapping("/noticias") //endpoint - 
    public String listarNoticias (Model model) {
        model.addAttribute("noticias", NoticiaRepository.findAll());
        return "index";
    }

    
}
