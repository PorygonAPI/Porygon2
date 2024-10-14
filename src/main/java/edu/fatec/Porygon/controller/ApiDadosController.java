package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.repository.ApiDadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/dados")
public class ApiDadosController {

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    @GetMapping("/{id}")
    public ResponseEntity<String> abrirDados(@PathVariable Integer id) {
        Optional<ApiDados> apiDadosOptional = apiDadosRepository.findById(id);

        if (apiDadosOptional.isPresent()) {
            ApiDados apiDados = apiDadosOptional.get();
            String conteudo = apiDados.getConteudo();
            String descricao = apiDados.getDescricao();

            String tipo = descricao.toLowerCase();
            String conteudoFormatado = formatarConteudo(conteudo, tipo);

            return ResponseEntity.ok(conteudoFormatado);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dados não encontrados");
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
