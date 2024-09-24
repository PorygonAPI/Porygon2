package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.service.DataScrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ScrapperController {

    @Autowired
    private DataScrapper dataScrapper;

//    //testes - WebScrapper
//    // Endpoint para testar a conexão com uma URL e retornar o conteúdo textual
//    // ex: http://localhost:8080/test-connection?url=https://www.canalrural.com.br/
//    @GetMapping("/test-connection")
//    public String testConnection(@RequestParam String url) {
//        return dataScrapper.testConnection(url);
//    }
//
//    // Endpoint para testar a extração de links da página principal de uma URL
//    // ex: http://localhost:8080/test-parse-main-page?url=https://www.canalrural.com.br/
//    @GetMapping("/test-parse-main-page")
//    public List<String> testParseMainPage(@RequestParam String url) {
//        return dataScrapper.testParseMainPage(url);
//    }
//
//    // Endpoint para testar o scraping de portais no banco de dados e retornar os títulos das páginas
//    // ex: http://localhost:8080/scrapper-test-portals
//    @GetMapping("/scrapper-test-portals")
//    public List<String> scrapperTestPortals() {
//        return dataScrapper.ScrapperTestPortals();
//    }

    // Endpoint to trigger the scraping process by Portal ID
    @GetMapping("/scrape/{id}")
    public String scrapeDataByPortalID(@PathVariable int id) {
        try {
            dataScrapper.scrapeDatabyPortalID(id);
            return "Scraping process completed for portal ID: " + id;
        } catch (Exception e) {
            return "Error during scraping: " + e.getMessage();
        }
    }
}
