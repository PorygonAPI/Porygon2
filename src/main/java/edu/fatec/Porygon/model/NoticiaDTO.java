package edu.fatec.Porygon.model;

import java.util.Date;

public class NoticiaDTO {
    private Integer id;
    private String titulo;
    private Date data;
    private String conteudo;
    private String href;
    private String jornalista;

    public NoticiaDTO(Noticia noticia) {
        this.id = noticia.getId();
        this.titulo = noticia.getTitulo();
        this.data = noticia.getData();
        this.conteudo = noticia.getConteudo();
        this.href = noticia.getHref();
        this.jornalista = noticia.getJornalista() != null ? noticia.getJornalista().getNome() : "Desconhecido";
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getJornalista() {
        return jornalista;
    }

    public void setJornalista(String jornalista) {
        this.jornalista = jornalista;
    }


}