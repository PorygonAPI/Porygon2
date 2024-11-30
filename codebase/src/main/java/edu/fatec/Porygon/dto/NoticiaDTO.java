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
    private String jornalista;
    private String portal;  // Novo campo para o nome do portal
    private List<TagDTO> tags;

    // Construtor que agora também preenche o nome do portal
    public NoticiaDTO(Noticia noticia) {
        this.id = noticia.getId();
        this.titulo = noticia.getTitulo();
        this.data = noticia.getData();
        this.conteudo = noticia.getConteudo();
        this.jornalista = noticia.getJornalista().getNome();
        this.portal = noticia.getPortal() != null ? noticia.getPortal().getNome() : null; // Preenche o nome do portal
        this.tags = noticia.getTags().stream()
                .map(TagDTO::new)
                .collect(Collectors.toList());
    }

    // Getters e Setters

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

    public String getJornalista() {
        return jornalista;
    }

    public void setJornalista(String jornalista) {
        this.jornalista = jornalista;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
