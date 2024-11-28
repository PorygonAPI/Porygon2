package edu.fatec.Porygon.dto;

import java.util.List;
import java.util.stream.Collectors;
import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.model.Api;
import java.time.LocalDate;

public class ApiDadosDTO {
    private Integer id;
    private String conteudo;
    private List<TagDTO> tags;
    private ApiDTO api;
    private LocalDate dataColeta;

    public ApiDadosDTO(ApiDados apiDados, String conteudoFormatado) {
        this.id = apiDados.getId();
        this.conteudo = conteudoFormatado;
        this.tags = apiDados.getTags().stream()
                .map(TagDTO::new)
                .collect(Collectors.toList());
        this.api = new ApiDTO(apiDados.getApi());
        this.dataColeta = apiDados.getDataColeta();
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

    public ApiDTO getApi() {
        return api;
    }

    public void setApi(ApiDTO api) {
        this.api = api;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }
}
