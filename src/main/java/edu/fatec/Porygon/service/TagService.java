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

    @Autowired
    private TagScrapperService tagScrapperService;

    public Tag criarTag(Tag tag) {
        if (tagRepository.existsByNome(tag.getNome())) {
            throw new RuntimeException("A tag já existe.");
        }

        Tag novaTag = tagRepository.save(tag);
        List<String> sinonimos = tagScrapperService.buscarSinonimos(novaTag.getNome());
        vincularSinonimos(sinonimos, novaTag);

        return novaTag;
    }

    public Tag editarTag(Integer id, String novoNome) {
        Tag tagExistente = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada."));

        if (!tagExistente.getNome().equalsIgnoreCase(novoNome) && tagRepository.existsByNome(novoNome)) {
            throw new RuntimeException("Uma tag com este nome já existe.");
        }

        tagExistente.setNome(novoNome);
        tagRepository.save(tagExistente);

        List<String> sinonimos = tagScrapperService.buscarSinonimos(tagExistente.getNome());
        vincularSinonimos(sinonimos, tagExistente);

        return tagExistente;
    }

    private void vincularSinonimos(List<String> sinonimos, Tag tag) {
        if (sinonimos != null && !sinonimos.isEmpty()) {
            List<Sinonimo> sinonimoList = new ArrayList<>();
            for (String sinonimoNome : sinonimos) {
                // Verifica a existência considerando apenas a combinação com a mesma tag
                if (!sinonimoRepository.existsByNomeAndTag(sinonimoNome, tag)) {
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

    public Tag buscarTagPorId(Integer id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada."));
    }
}
