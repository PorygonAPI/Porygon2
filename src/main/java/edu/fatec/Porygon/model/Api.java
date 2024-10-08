package edu.fatec.Porygon.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Api {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String descricao;
    private String url;
    private String formato;

    @ManyToOne
    @JoinColumn(name = "agendador_id")
    private Agendador agendador;

    private boolean ativo;

    @ManyToMany
    @JoinTable(
        name = "api_tag", 
        joinColumns = @JoinColumn(name = "api_id"), 
        inverseJoinColumns = @JoinColumn(name = "tag_id") 
    )
    private List<Tag> tags;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Agendador getAgendador() {
        return agendador;
    }

    public void setAgendador(Agendador agendador) {
        this.agendador = agendador;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}


