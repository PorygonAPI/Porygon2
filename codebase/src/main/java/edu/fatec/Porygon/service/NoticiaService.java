package edu.fatec.Porygon.service;

import java.text.Normalizer;
import java.time.LocalDate;

import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.SinonimoRepository;
import edu.fatec.Porygon.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@EnableAsync
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

    public Page<Noticia> listarNoticiasPorFiltros(LocalDate dataInicio, LocalDate dataFim, List<Integer> tagIds, Pageable pageable) {
        if (dataInicio != null && dataFim != null && tagIds != null && !tagIds.isEmpty()) {
            return noticiaRepository.findByDataBetweenAndTagsIn(dataInicio, dataFim, tagIds, pageable);
        } else if (dataInicio != null && dataFim != null) {
            return noticiaRepository.searchNewsByData(dataInicio, dataFim, pageable);
        } else if (tagIds != null && !tagIds.isEmpty()) {
            return noticiaRepository.findByTags(tagIds, pageable);
        } else {
            return noticiaRepository.findAll(pageable);
        }
    }

    public Noticia salvar(Noticia noticia) {
        Set<Tag> foundTags = associarTagsAsync(noticia).join();

        if (foundTags.isEmpty()) {
            return null;
        }

        noticia.setTags(foundTags);
        return noticiaRepository.save(noticia);
    }

    @Async
    public CompletableFuture<Set<Tag>> associarTagsAsync(Noticia noticia) {
        Set<Tag> tagsPortal = new HashSet<>(tagRepository.findAllByPortais_Id(noticia.getPortal().getId()));

        CompletableFuture<Set<Tag>> foundTagsTitulo = encontrarTagsNoTituloAsync(noticia.getTitulo(), tagsPortal);
        CompletableFuture<Set<Tag>> foundTagsConteudo = encontrarTagsNoConteudoAsync(noticia.getConteudo(), tagsPortal);
        CompletableFuture<Set<Tag>> foundTagsSinonimos = encontrarSinonimosAsync(noticia.getTitulo(), noticia.getConteudo(), tagsPortal);
  
        return foundTagsTitulo.thenCombine(foundTagsConteudo, (tagsTitulo, tagsConteudo) -> {
            tagsTitulo.addAll(tagsConteudo); 
            return tagsTitulo;
        }).thenCombine(foundTagsSinonimos, (tagsCombinadas, tagsSinonimos) -> {
            tagsCombinadas.addAll(tagsSinonimos);
            return tagsCombinadas;
        });
    }
    
    @Async
    public CompletableFuture<Set<Tag>> encontrarTagsNoTituloAsync(String titulo, Set<Tag> tagsPortal) {
        Set<Tag> foundTags = new HashSet<>();
        String tituloNormalizado = normalizarTexto(titulo); 
        for (Tag tag : tagsPortal) {
            String tagNormalizada = normalizarTexto(tag.getNome()); 
            if (tituloNormalizado.contains(tagNormalizada)) {
                foundTags.add(tag);
            }
        }
        return CompletableFuture.completedFuture(foundTags);
    }

    @Async
    public CompletableFuture<Set<Tag>> encontrarTagsNoConteudoAsync(String conteudo, Set<Tag> tagsPortal) {
        Set<Tag> foundTags = new HashSet<>();
        String conteudoNormalizado = normalizarTexto(conteudo);
        for (Tag tag : tagsPortal) {
            String tagNormalizada = normalizarTexto(tag.getNome()); 
            if (conteudoNormalizado.contains(tagNormalizada)) {
                foundTags.add(tag);
            }
        }
        return CompletableFuture.completedFuture(foundTags);
    }

    @Async
    public CompletableFuture<Set<Tag>> encontrarSinonimosAsync(String titulo, String conteudo, Set<Tag> tagsPortal) {
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
        return CompletableFuture.completedFuture(foundTags);
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