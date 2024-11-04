package edu.fatec.Porygon.model;

public class NoticiaDTO {
    private Integer id;
    private String titulo;
    private String data;
    private String conteudo;
    private String jornalista;

    // Construtor
    public NoticiaDTO(Integer id, String titulo, String data, String conteudo, String jornalista) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.conteudo = conteudo;
        this.jornalista = jornalista;
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getData() {
        return data;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String getJornalista() {
        return jornalista;
    }
}