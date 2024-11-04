package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.repository.ApiDadosRepository;
import edu.fatec.Porygon.service.ApiDadosService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ApiDadosController {

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    @Autowired
    private ApiDadosService apiDadosService;


    @GetMapping("/apis/dados")
    public String listarApiDados(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("api.nome").ascending());
        Page<ApiDados> apiDadosPage = apiDadosService.listarApiDados(pageable);

        model.addAttribute("apiDadosList", apiDadosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", apiDadosPage.getTotalPages());
        model.addAttribute("totalItems", apiDadosPage.getTotalElements());
        return "apiDados";
    }

    @GetMapping("/dados/{id}")
    public ResponseEntity<String> abrirDados(@PathVariable Long id) {
        Optional<ApiDados> apiDadosOptional = apiDadosRepository.findById(id);

        if (apiDadosOptional.isPresent()) {
            ApiDados apiDados = apiDadosOptional.get();
            String conteudo = apiDados.getConteudo();

            Integer formatoId = apiDados.getApi().getFormato().getId();

            String tipoFormato = getTipoFormato(formatoId);
            String conteudoFormatado = formatarConteudo(conteudo, tipoFormato);

            return ResponseEntity.ok(conteudoFormatado);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados não encontrados");
    }

    private String getTipoFormato(Integer formatoId) {
        switch (formatoId) {
            case 1:
                return "json";
            case 2:
                return "csv";
            case 3:
                return "xml";
            default:
                return "desconhecido";
        }
    }

    private String formatarConteudo(String conteudo, String tipoFormato) {
        switch (tipoFormato) {
            case "json":
                return formatarComoJson(conteudo);
            case "xml":
                return formatarComoXml(conteudo);
            case "csv":
                return formatarComoCsv(conteudo);
            default:
                return conteudo;
        }
    }

    private String formatarComoJson(String conteudo) {
        return conteudo
                .replace("{", "\n{")
                .replace("}", "}\n")
                .replace("[", "\n[")
                .replace("]", "]\n")
                .replace(",", ",\n")
                .replace(":", ": ");
    }

    private String formatarComoCsv(String conteudo) {
        return conteudo.replace(";", ";\n");
    }

    private String formatarComoXml(String conteudo) {
        return conteudo
                .replace("<", "\n<")
                .replace(">", ">\n")
                .replace("<rate>", "\n<rate>")
                .replace("</rate>", "</rate>\n")
                .replace("\n\n", "\n");
    }

}