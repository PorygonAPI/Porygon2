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
import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SinonimoRepository sinonimoRepository;

    public Tag criarTag(Tag tag) {
        if (tagRepository.existsByNome(tag.getNome())) {
            throw new IllegalArgumentException("A tag já existe.");
        }

        Tag novaTag = tagRepository.save(tag);
        List<String> sinonimos = TagScrapperService.buscarSinonimos(novaTag.getNome());
        vincularSinonimos(sinonimos, novaTag);

        return novaTag;
    }

    public Tag editarTag(Integer id, String novoNome) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isEmpty()) {
            throw new IllegalArgumentException("Tag não encontrada.");
        }

        Tag tagExistente = optionalTag.get();
        if (!tagExistente.getNome().equalsIgnoreCase(novoNome) && tagRepository.existsByNome(novoNome)) {
            throw new IllegalArgumentException("Uma tag com este nome já existe.");
        }

        tagExistente.setNome(novoNome);
        tagRepository.save(tagExistente);

        List<String> sinonimos = TagScrapperService.buscarSinonimos(tagExistente.getNome());
        vincularSinonimos(sinonimos, tagExistente);

        return tagExistente;
    }

    private void vincularSinonimos(List<String> sinonimos, Tag tag) {
        if (sinonimos != null && !sinonimos.isEmpty()) {
            List<Sinonimo> sinonimoList = new ArrayList<>();
            for (String sinonimoNome : sinonimos) {
                if (!sinonimoRepository.existsByNome(sinonimoNome)) {
                    Sinonimo sinonimo = new Sinonimo();
                    sinonimo.setNome(sinonimoNome);
                    sinonimo.setTag(tag);
                    sinonimoList.add(sinonimo);
                }
            }
            sinonimoRepository.saveAll(sinonimoList);
            tag.setSinonimos(sinonimoList);
        } else {
            tag.setSinonimos(new ArrayList<>());
        }
    }

    public List<Tag> listarTagsOrdenadas() {
        return tagRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }
}
