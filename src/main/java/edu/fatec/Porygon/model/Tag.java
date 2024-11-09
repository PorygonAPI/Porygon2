package edu.fatec.Porygon.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 46)
    private String nome;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sinonimo> sinonimos;

    @ManyToMany(mappedBy = "tags")
    private Set<Api> apis =  new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    private Set<Portal> portais = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    private Set<Noticia> noticias = new HashSet<>();

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

    public List<Sinonimo> getSinonimos() {
        return sinonimos;
    }

    public void setSinonimos(List<Sinonimo> sinonimos) {
        this.sinonimos = sinonimos;
    }

    public Set<Api> getApis() {
        return apis;
    }

    public void setApis(Set<Api> apis) {
        this.apis = apis;
    }

    public Set<Portal> getPortais() {
        return portais;
    }

    public void setPortais(Set<Portal> portais) {
        this.portais = portais;
    }

    public Set<Noticia> getNoticias() {
        return noticias;
    }

    public void setNoticias(Set<Noticia> noticias) {
        this.noticias = noticias;
    }

}