package edu.fatec.Porygon.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Agendador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String tipo;
    private Integer quantidade;

    @OneToMany(mappedBy = "agendador")
    private List<Portal> portais;
    
    @OneToMany(mappedBy = "agendador")
    private List<Api> apis;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}


