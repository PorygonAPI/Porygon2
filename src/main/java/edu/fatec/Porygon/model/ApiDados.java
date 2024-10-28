package edu.fatec.Porygon.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class ApiDados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String conteudo;
    private String descricao;

    private LocalDate dataColeta;

    @ManyToOne
    @JoinColumn(name = "api_id")
    private Api api;
    public String getConteudo() {
        return conteudo;
    }
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Api getApi() {
        return api;
    }
    public void setApi(Api api) {
        this.api = api;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDate getDataColeta() {
        return dataColeta;
    }
    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }
}