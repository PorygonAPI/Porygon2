package edu.fatec.Porygon.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
public class Portal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String url;
    @Column(length = 254)
    private String seletorJornalista;
    @Column(length = 254)
    private String seletorDataPublicacao;
    @Column(length = 254)
    private String seletorConteudo;
    @Column(length = 254)
    private String seletorTitulo;
    @Column(length = 254)
    private String seletorCaminhoNoticia;
    private LocalDate ultimaAtualizacao;
    private LocalDate dataCriacao;

    @ManyToOne
    @JoinColumn(name = "agendador_id")
    private Agendador agendador;

    private boolean ativo;

    private boolean hasScrapedToday;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Portal_Tag",
            joinColumns = @JoinColumn(name = "portal_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @OneToMany(mappedBy = "portal")
    @JsonManagedReference
    private List<Noticia> noticias;;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public List<Noticia> getNoticias() {
        return noticias;
    }

    public void setNoticias(List<Noticia> noticias) {
        this.noticias = noticias;
    }

    public String getSeletorJornalista() {
        return seletorJornalista;
    }

    public void setSeletorJornalista(String seletorJornalista) {
        this.seletorJornalista = seletorJornalista;
    }

    public String getSeletorDataPublicacao() {
        return seletorDataPublicacao;
    }

    public void setSeletorDataPublicacao(String seletorDataPublicacao) {
        this.seletorDataPublicacao = seletorDataPublicacao;
    }

    public String getSeletorConteudo() {
        return seletorConteudo;
    }

    public void setSeletorConteudo(String seletorConteudo) {
        this.seletorConteudo = seletorConteudo;
    }

    public String getSeletorTitulo() {
        return seletorTitulo;
    }

    public void setSeletorTitulo(String seletorTitulo) {
        this.seletorTitulo = seletorTitulo;
    }

    public String getSeletorCaminhoNoticia() {
        return seletorCaminhoNoticia;
    }

    public void setSeletorCaminhoNoticia(String seletorCaminhoNoticia) {
        this.seletorCaminhoNoticia = seletorCaminhoNoticia;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDate ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public boolean isHasScrapedToday() {
        return hasScrapedToday;
    }

    public void setHasScrapedToday(boolean hasScrapedToday) {
        this.hasScrapedToday = hasScrapedToday;
    }
}