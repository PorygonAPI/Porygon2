package edu.fatec.Porygon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.*;
import edu.fatec.Porygon.repository.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApiDadosService {

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SinonimoRepository sinonimoRepository;

    public List<ApiDados> listarApiDados() {
        return apiDadosRepository.findAll();
    }
    public List<ApiDados> buscarApiDados(LocalDate dataInicio, LocalDate dataFim, List<Integer> tagIds) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
        }

        if ((dataInicio == null || dataFim == null) && (tagIds == null || tagIds.isEmpty())) {
            return listarApiDados();
        }

        List<Integer> totalTagIds = new ArrayList<>();
        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> selectedTags = tagRepository.findAllById(tagIds);

            List<Sinonimo> sinonimos = sinonimoRepository.findByTagIn(selectedTags);
            List<String> sinonimoNames = sinonimos.stream()
                    .map(Sinonimo::getNome)
                    .collect(Collectors.toList());

            List<Tag> synonymTags = tagRepository.findByNomeInIgnoreCase(sinonimoNames);

            Set<Tag> totalTags = new HashSet<>();
            totalTags.addAll(selectedTags);
            totalTags.addAll(synonymTags);

            totalTagIds = totalTags.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
        }

        if (dataInicio != null && dataFim != null && !totalTagIds.isEmpty()) {
            return apiDadosRepository.findDistinctByDataColetaBetweenAndTags_IdIn(dataInicio, dataFim, totalTagIds);
        } else if (dataInicio != null && dataFim != null) {
            return apiDadosRepository.findByDataColetaBetween(dataInicio, dataFim);
        } else if (!totalTagIds.isEmpty()) {
            return apiDadosRepository.findDistinctByTags_IdIn(totalTagIds);
        }

        return Collections.emptyList();
    }
}

