package edu.fatec.Porygon.dto;

import edu.fatec.Porygon.model.Tag;

public class TagDTO {
    private Integer id;
    private String nome;

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.nome = tag.getNome();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}