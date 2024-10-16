package edu.fatec.Porygon.controller;
import edu.fatec.Porygon.repository.ApiRepository;
import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.model.Api;
import edu.fatec.Porygon.repository.ApiDadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiDadosController {

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    @Autowired
    private ApiRepository apiRepository;

    @GetMapping("/dados/{id}")
    public ResponseEntity<String> abrirDados(@PathVariable Integer id) {
        Optional<Api> apiOptional = apiRepository.findById(id);

        if (apiOptional.isPresent()) {
            Api api = apiOptional.get();

            if (!api.isAtivo()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados não encontrados");
            }

            Optional<ApiDados> apiDadosOptional = apiDadosRepository.findByApiId(id);

            if (apiDadosOptional.isPresent()) {
                ApiDados apiDados = apiDadosOptional.get();
                String conteudo = apiDados.getConteudo();

                Integer formatoId = apiDados.getApi().getFormato().getId();  // Pegando o formato pela api

                String tipo = getTipoFormato(formatoId);
                String conteudoFormatado = formatarConteudo(conteudo, tipo);

                return ResponseEntity.ok(conteudoFormatado);
            }
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

    private String formatarConteudo(String conteudo, String tipo) {
        switch (tipo) {
            case "json":
                return conteudo
                        .replace("{", "\n{")
                        .replace("}", "}\n")
                        .replace("[", "\n[")
                        .replace("]", "]\n")
                        .replace(",", ",\n")
                        .replace(":", ": ");
            case "xml":
                return conteudo
                        .replace("<", "\n<")
                        .replace(">", ">\n")
                        .replace("<rate>", "\n<rate>")
                        .replace("</rate>", "</rate>\n")
                        .replace("\n\n", "\n");
            case "csv":
                return conteudo.replace(";", ";\n");
            default:
                return conteudo;
        }
    }
}