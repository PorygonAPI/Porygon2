package edu.fatec.Porygon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.repository.TagRepository;
import edu.fatec.Porygon.repository.SinonimoRepository;
import java.util.List;
import java.util.ArrayList;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SinonimoRepository sinonimoRepository;

    public Tag criarTag(Tag tag) {

        String newTagName = StandardizeTag(tag);

        if (tagRepository.existsByNome(newTagName)) {
            throw new IllegalArgumentException("A tag já existe.");
        }

        tag.setNome(newTagName);

        Tag novaTag = tagRepository.save(tag);
    
        List<String> sinonimos = TagScrapperService.buscarSinonimos(novaTag.getNome());
    
        if (sinonimos != null && !sinonimos.isEmpty()) {
            List<Sinonimo> sinonimoList = new ArrayList<>();
            for (String sinonimoNome : sinonimos) {
                if (!sinonimoRepository.existsByNome(sinonimoNome)) {
                    Sinonimo sinonimo = new Sinonimo();
                    sinonimo.setNome(sinonimoNome);
                    sinonimo.setTag(novaTag);
                    sinonimoList.add(sinonimo);
                }
            }
            sinonimoRepository.saveAll(sinonimoList);
            novaTag.setSinonimos(sinonimoList);
        } else {
            novaTag.setSinonimos(new ArrayList<>()); 
        }
        return novaTag;
    }
    public List<Tag> listarTagsOrdenadas() {
        return tagRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    public String StandardizeTag(Tag tag) {
        String modifiedTag = tag.getNome();
        String returnTag = "";

        if (modifiedTag == null || modifiedTag.isEmpty()) {
            returnTag = modifiedTag;
        }else{
            String[] nameWords = modifiedTag.replaceAll("-"," ").toLowerCase().split("\\s+");
            StringBuilder newString = new StringBuilder();
            for (String word : nameWords) {
                if (!word.isEmpty()) {
                    newString.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
                }
            }
            returnTag = newString.toString().trim();
        }

        return returnTag;
    }

}