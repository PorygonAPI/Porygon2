package edu.fatec.Porygon.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Formato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome; 

    @OneToMany(mappedBy = "formato")
    private List<Api> apis; 

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

    public List<Api> getApis() {
        return apis;
    }

    public void setApis(List<Api> apis) {
        this.apis = apis;
    }
}
