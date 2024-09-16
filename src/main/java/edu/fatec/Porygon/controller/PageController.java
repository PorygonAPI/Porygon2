package edu.fatec.Porygon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

    @GetMapping("/portal")
    public String portal() {
        return "portal"; // Isso retornará portal.html localizado em src/main/resources/templates/
    }
    
    @GetMapping("/index")
    public String index() {
        return "index"; // Isso retornará index.html localizado em src/main/resources/templates/
    }
}


