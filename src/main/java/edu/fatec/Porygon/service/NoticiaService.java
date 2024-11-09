package edu.fatec.Porygon.service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.NoticiaRepository;
import edu.fatec.Porygon.repository.TagRepository;
import edu.fatec.Porygon.repository.SinonimoRepository;

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

    public Noticia salvar(Noticia noticia, List<Integer> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            Set<Tag> tagsPortal = new HashSet<>(tagRepository.findAllById(tagIds));
            associarTagsRelevantes(noticia, tagsPortal);
        }
        return noticiaRepository.save(noticia);
    }

    public Noticia atualizarTags(Integer noticiaId, List<Integer> tagIds) {
        Noticia noticia = noticiaRepository.findById(noticiaId)
                .orElseThrow(() -> new RuntimeException("Notícia não encontrada"));

        if (tagIds != null) {
            Set<Tag> tagsPortal = tagIds.isEmpty()
                    ? new HashSet<>()
                    : new HashSet<>(tagRepository.findAllById(tagIds));
            associarTagsRelevantes(noticia, tagsPortal);
        }

        return noticiaRepository.save(noticia);
    }
    public void findTagsInTitle() {
        List<Tag> tags = tagRepository.findAll();
        List<Noticia> news = noticiaRepository.findAll();

        for (Noticia noticia : news) {
            HashSet<Tag> foundTags = new HashSet<>();
            for (Tag tag : tags) {
                if (noticia.getTitulo().toLowerCase().contains(tag.getNome().toLowerCase())) {
                    foundTags.add(tag);
                } else {
                    Tag tagFound = searchSynonymsInTitle(tag, noticia);
                    if (tagFound != null) {
                        foundTags.add(tagFound);
                    }
                }
            }

            if (!foundTags.isEmpty()) {
                noticia.setTags(new HashSet<>(foundTags));
                noticiaRepository.save(noticia);
            }
        }
    }

    public Tag searchSynonymsInTitle(Tag tag, Noticia noticia) {
        List<Sinonimo> sinonimos = sinonimoRepository.findByTag(tag);
        Tag retorno = null;

        String cleanTitle = normalizarTexto(noticia.getTitulo());
        String[] words = cleanTitle.split("\\s+");

        outerLoop:
        for (String word : words) {
            for (Sinonimo sinonimo : sinonimos) {
                if (sinonimo.getNome().toLowerCase().equals(word)) {
                    retorno = tag;
                    break outerLoop;
                }
            }
        }

        return retorno;
    }

    private void associarTagsRelevantes(Noticia noticia, Set<Tag> tagsPortal) {
        Set<Tag> tagsRelevantes = new HashSet<>();

        String conteudoNormalizado = normalizarTexto(noticia.getConteudo());
        String tituloNormalizado = normalizarTexto(noticia.getTitulo());

        for (Tag tag : tagsPortal) {
            String tagNormalizada = normalizarTexto(tag.getNome());
            String[] palavrasTag = tagNormalizada.split("[-\\s]");
            boolean tagEncontrada = false;

            if (palavrasTag.length > 1) {
                tagEncontrada = Arrays.stream(palavrasTag)
                        .allMatch(palavra -> conteudoNormalizado.contains(palavra) ||
                                tituloNormalizado.contains(palavra));
            } else {
                tagEncontrada = conteudoNormalizado.contains(tagNormalizada) ||
                        tituloNormalizado.contains(tagNormalizada);
            }

            if (!tagEncontrada && tag.getSinonimos() != null) {
                for (Sinonimo sinonimo : tag.getSinonimos()) {
                    String sinonimoNormalizado = normalizarTexto(sinonimo.getNome());
                    if (conteudoNormalizado.contains(sinonimoNormalizado) ||
                            tituloNormalizado.contains(sinonimoNormalizado)) {
                        tagEncontrada = true;
                        break;
                    }
                }
            }

            if (tagEncontrada) {
                tagsRelevantes.add(tag);
            }
        }

        noticia.setTags(tagsRelevantes);
    }

    private String normalizarTexto(String texto) {
        if (texto == null)
            return "";
        return texto.toLowerCase()
                .replaceAll("[áàãâä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i")
                .replaceAll("[óòõôö]", "o")
                .replaceAll("[úùûü]", "u")
                .replaceAll("[ç]", "c")
                .replaceAll("[^a-z0-9\\s-]", "");
    }
}