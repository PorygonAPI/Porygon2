package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.dto.ApiDadosDTO;
import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.repository.ApiDadosRepository;
import edu.fatec.Porygon.service.ApiDadosService;
import edu.fatec.Porygon.service.TagService;
import edu.fatec.Porygon.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiDadosController {

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    @Autowired
    private ApiDadosService apiDadosService;

    @Autowired
    private TagService tagService;

    @GetMapping("/apis/dados")
    public String listarApiDados(
            @RequestParam(required = false) List<Integer> tagIds,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Model model) {
        List<ApiDados> apiDadosList = apiDadosService.buscarApiDados(dataInicio, dataFim, tagIds);

        model.addAttribute("apiDadosList", apiDadosList);

        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);

        List<Tag> tags = tagService.listarTagsOrdenadas();
        model.addAttribute("tags", tags);
        model.addAttribute("selectedTagIds", tagIds);

        return "apiDados";
    }

    @GetMapping("/apiDados")
    @ResponseBody
    public ResponseEntity<?> listarApiDados(
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(value = "tagIds", required = false) List<Integer> tagIds,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        
        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data final não pode ser anterior à data inicial.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ApiDados> apiDadosPage = apiDadosService.buscarApiDados(dataInicio, dataFim, tagIds, pageable);

        if (apiDadosPage.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("mensagem", "Nenhum dado encontrado"));
        }

        List<ApiDadosDTO> apiDadosDTOs = apiDadosPage.getContent().stream()
                .map(apiDados -> new ApiDadosDTO(apiDados, apiDados.getConteudo()))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("content", apiDadosDTOs);
        response.put("totalPages", apiDadosPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dados/{id}")
    @ResponseBody
    public ResponseEntity<?> abrirDados(@PathVariable Long id) {
        Optional<ApiDados> apiDadosOptional = apiDadosRepository.findById(id);
    
        if (apiDadosOptional.isPresent()) {
            ApiDados apiDados = apiDadosOptional.get();
            String conteudo = apiDados.getConteudo();
    
            Integer formatoId = apiDados.getApi().getFormato().getId();
            String tipoFormato = getTipoFormato(formatoId);
            String conteudoFormatado = formatarConteudo(conteudo, tipoFormato);
    
            ApiDadosDTO response = new ApiDadosDTO(apiDados, conteudoFormatado);
            return ResponseEntity.ok(response);
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