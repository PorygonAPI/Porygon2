package edu.fatec.Porygon.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import edu.fatec.Porygon.model.Noticia;

public class NoticiaDTO {
    private Integer id;
    private String titulo;
    private Date data;
    private String conteudo;
    private String autor;
    private List<TagDTO> tags;

    public NoticiaDTO(Noticia noticia) {
        this.id = noticia.getId();
        this.titulo = noticia.getTitulo();
        this.data = noticia.getData();
        this.conteudo = noticia.getConteudo();
        this.autor = noticia.getAutor();
        this.tags = noticia.getTags().stream()
                            .map(TagDTO::new)
                            .collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }


}