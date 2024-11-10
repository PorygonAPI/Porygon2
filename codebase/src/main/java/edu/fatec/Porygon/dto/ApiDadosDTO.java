package edu.fatec.Porygon.dto;

import java.util.List;
import java.util.stream.Collectors;
import edu.fatec.Porygon.model.ApiDados;

public class ApiDadosDTO {
    private Integer id;
    private String conteudo;
    private List<TagDTO> tags;

    public ApiDadosDTO(ApiDados apiDados, String conteudoFormatado) {
        this.id = apiDados.getId();
        this.conteudo = conteudoFormatado;
        this.tags = apiDados.getTags().stream()
                .map(TagDTO::new)
                .collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }


}
