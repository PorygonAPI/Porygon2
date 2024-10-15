package edu.fatec.Porygon.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
// import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Jornalista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @OneToMany(mappedBy = "jornalista")
    @JsonIgnore
    private List<Noticia> noticias;

    // @OneToMany(mappedBy = "jornalista", cascade = CascadeType.ALL)
    // @JsonManagedReference // Controla a serialização da lista de notícias
    // private List<Noticia> noticias;

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

    public List<Noticia> getNoticias() {
        return noticias;
    }

    public void setNoticias(List<Noticia> noticias) {
        this.noticias = noticias;
    }
}

