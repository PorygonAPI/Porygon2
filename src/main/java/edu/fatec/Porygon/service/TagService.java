package edu.fatec.Porygon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.TagRepository;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag criarTag(Tag tag) {
        String sinonimos = TagScrapperService.buscarSinonimo(tag.getNome());
        if (sinonimos != null && !sinonimos.isEmpty()) {
            tag.setNomeSinonimo(sinonimos);
        }
        
        return tagRepository.save(tag);
    }
}
