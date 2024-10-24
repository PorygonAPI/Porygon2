package edu.fatec.Porygon.service;

import org.springframework.beans.factory.annotation.Autowired;
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
        if (tagRepository.existsByNome(tag.getNome())) {
            throw new RuntimeException("Já existe uma tag com esse nome.");
        }
    
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
}