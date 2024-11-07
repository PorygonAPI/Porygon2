package edu.fatec.Porygon.service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public void findTagsInTitle() {
        List<Tag> tags = tagRepository.findAll();
        List<Noticia> news = noticiaRepository.findAll();

        for (Noticia noticia : news) {
            HashSet<Tag> foundTags = new HashSet<>();
            for (Tag tag : tags) {
                if(noticia.getTitulo().toLowerCase().contains(tag.getNome().toLowerCase())){
                    foundTags.add(tag);
                }else{
                    Tag tagFound = searchSynonymsInTitle(tag, noticia);
                    if (tagFound != null) {
                        foundTags.add(tagFound);
                    }
                }
            }

            if(!foundTags.isEmpty()) {
                noticia.setTags(new ArrayList<>(foundTags));
                noticiaRepository.save(noticia);
            }
        }
    }

    public Tag searchSynonymsInTitle(Tag tag, Noticia noticia) {
        List<Sinonimo> sinonimos = sinonimoRepository.findByTag(tag);
        Tag retorno = null;

        String cleanTitleAccent = Normalizer.normalize(noticia.getTitulo(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}", "");
        String cleanTitleSpecialCharacter = cleanTitleAccent.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        String[] words = cleanTitleSpecialCharacter.split("\\s+");

        outerLoop:
        for (String word : words) {
            for(Sinonimo sinonimo : sinonimos) {
                if (sinonimo.getNome().toLowerCase().equals(word)){
                    retorno=tag;
                    break outerLoop;
                }
            }
        }

        return retorno;
    }
}
