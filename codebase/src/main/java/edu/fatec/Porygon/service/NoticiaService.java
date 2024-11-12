package edu.fatec.Porygon.service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.*;

import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.SinonimoRepository;
import edu.fatec.Porygon.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SinonimoRepository sinonimoRepository;

    public List<Noticia> listarNoticias() {
        return noticiaRepository.findAll();
    }

    public List<Noticia> listarNoticiasPorData(LocalDate dataInicio, LocalDate dataFim) {
        return noticiaRepository.searchNewsByData(dataInicio, dataFim);
    }

    public Noticia salvar(Noticia noticia) {
        Set<Tag> foundTags = associarTags(noticia);

        if (foundTags.isEmpty()) {
            System.out.println("Notícia não possui tags, não será salva.");
            return null;
        }

        noticia.setTags(foundTags);
        return noticiaRepository.save(noticia);
    }

    private Set<Tag> associarTags(Noticia noticia) {
        Set<Tag> tagsPortal = new HashSet<>(tagRepository.findAllByPortais_Id(noticia.getPortal().getId()));

        Set<Tag> foundTags = encontrarTagsNoTitulo(noticia.getTitulo(), tagsPortal);
        if (foundTags.isEmpty()) {
            foundTags = encontrarTagsNoConteudo(noticia.getConteudo(), tagsPortal);
        }
        if (foundTags.isEmpty()) {
            foundTags = encontrarSinonimos(noticia.getTitulo(), noticia.getConteudo(), tagsPortal);
        }

        return foundTags;
    }

    private Set<Tag> encontrarTagsNoTitulo(String titulo, Set<Tag> tagsPortal) {
        Set<Tag> foundTags = new HashSet<>();
        String tituloNormalizado = normalizarTexto(titulo); 
        for (Tag tag : tagsPortal) {
            String tagNormalizada = normalizarTexto(tag.getNome()); 
            if (tituloNormalizado.contains(tagNormalizada)) {
                foundTags.add(tag);
            }
        }
        return foundTags;
    }

    private Set<Tag> encontrarTagsNoConteudo(String conteudo, Set<Tag> tagsPortal) {
        Set<Tag> foundTags = new HashSet<>();
        String conteudoNormalizado = normalizarTexto(conteudo);
        for (Tag tag : tagsPortal) {
            String tagNormalizada = normalizarTexto(tag.getNome()); 
            if (conteudoNormalizado.contains(tagNormalizada)) {
                foundTags.add(tag);
            }
        }
        return foundTags;
    }

    private Set<Tag> encontrarSinonimos(String titulo, String conteudo, Set<Tag> tagsPortal) {
        Set<Tag> foundTags = new HashSet<>();
        String tituloNormalizado = normalizarTexto(titulo);
        String conteudoNormalizado = normalizarTexto(conteudo); 
        for (Tag tag : tagsPortal) {
            List<Sinonimo> sinonimos = sinonimoRepository.findByTag(tag);
            for (Sinonimo sinonimo : sinonimos) {
                String sinonimoNormalizado = normalizarTexto(sinonimo.getNome()); 
                if (tituloNormalizado.contains(sinonimoNormalizado) ||
                        conteudoNormalizado.contains(sinonimoNormalizado)) {
                    foundTags.add(tag);
                    break;
                }
            }
        }
        return foundTags;
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }

        texto = texto.toLowerCase();
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[^\\p{ASCII}]", "");

        return texto;
    }
}
