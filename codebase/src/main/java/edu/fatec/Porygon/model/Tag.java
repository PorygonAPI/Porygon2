package edu.fatec.Porygon.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String nomeSinonimo;

    @ManyToMany(mappedBy = "tags")
    private List<Api> apis;

    @ManyToMany(mappedBy = "tags")
    private List<Portal> portais;

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

    public String getNomeSinonimo() {
        return nomeSinonimo;
    }

    public void setNomeSinonimo(String nomeSinonimo) {
        this.nomeSinonimo = nomeSinonimo;
    }

    public List<Portal> getPortais() {
        return portais;
    }

    public void setPortais(List<Portal> portais) {
        this.portais = portais;
    }

    public List<Api> getApis() {
        return apis;
    }

    public void setApis(List<Api> apis) {
        this.apis = apis;
    }
}
