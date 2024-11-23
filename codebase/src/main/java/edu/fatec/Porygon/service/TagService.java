package edu.fatec.Porygon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.repository.TagRepository;
import edu.fatec.Porygon.repository.SinonimoRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SinonimoRepository sinonimoRepository;

    @Autowired
    private TagScrapperService tagScrapperService;

    public Tag criarTag(Tag tag, boolean forcarCadastro) {
        if (tagRepository.existsByNome(tag.getNome())) {
            throw new RuntimeException("A tag já existe.");
        }

        formatarPalavra(tag.getNome());
        tag.setNome(formatarPalavra(tag.getNome()));
        List<Sinonimo> sinonimosExistentes = sinonimoRepository.findByNome(tag.getNome().toLowerCase());

        if (!sinonimosExistentes.isEmpty() && !forcarCadastro) {
            throw new RuntimeException("A tag possui sinônimos cadastrados.");
        }

        Tag novaTag = tagRepository.save(tag);
        if (!sinonimosExistentes.isEmpty()) {
            for (Sinonimo sinonimo : sinonimosExistentes) {
                List<String> sinonimosNomes = sinonimoRepository.findByTag(sinonimo.getTag())
                        .stream()
                        .map(Sinonimo::getNome)
                        .collect(Collectors.toList());
                sinonimosNomes.add(sinonimo.getTag().getNome());
                vincularSinonimos(sinonimosNomes, novaTag);
            }
        } else {
            atualizarSinonimos(novaTag.getNome().toLowerCase(), novaTag);
        }
        return novaTag;
    }

    public Tag editarTag(Integer id, String novoNome) {
        Tag tagExistente = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada."));

        if (!tagExistente.getNome().equalsIgnoreCase(novoNome) && tagRepository.existsByNome(novoNome)) {
            throw new RuntimeException("Uma tag com este nome já existe.");}

        formatarPalavra(novoNome);
        tagExistente.setNome(formatarPalavra(novoNome));
        tagRepository.save(tagExistente);
        atualizarSinonimos(tagExistente.getNome().toLowerCase(), tagExistente);
        return tagExistente;}

    private String formatarPalavra(String nome) {
        String regex = "^[A-Za-zÀ-ÖØ-öø-ÿ]+([ -][A-Za-zÀ-ÖØ-öø-ÿ]+)*$";
        if (!nome.matches(regex)) {
            throw new IllegalArgumentException("Cadastre 1 (uma) palavra por vez ou palavras compostas com hífen ou espaço.");
        }

        StringBuilder tagFormatada = new StringBuilder();
        for (String palavra : nome.split(" ")) {
            String[] proxPalavra = palavra.split("-");
            for (int p = 0; p < proxPalavra.length; p++) {
                if (!proxPalavra[p].isEmpty()) {
                    proxPalavra[p] = proxPalavra[p].substring(0, 1).toUpperCase()
                            + proxPalavra[p].substring(1).toLowerCase();
                }
            }
            tagFormatada.append(String.join("-", proxPalavra)).append(" ");
        }
        return tagFormatada.toString().trim();
    }

    @Async
    public CompletableFuture<Void> atualizarSinonimos(String nomeTagParaScraping, Tag tag) {
        List<String> sinonimos = tagScrapperService.buscarSinonimos(nomeTagParaScraping);
        vincularSinonimos(sinonimos, tag);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> vincularSinonimos(List<String> sinonimos, Tag tag) {
        List<Sinonimo> sinonimosAntigos = sinonimoRepository.findByTag(tag);
        for (Sinonimo sinonimoAntigo : sinonimosAntigos) {
            if (!sinonimos.contains(sinonimoAntigo.getNome())) {
                sinonimoRepository.delete(sinonimoAntigo);
            }
        }

        for (String sinonimoNome : sinonimos) {
            String sinonimoFormatado = formatarPalavra(sinonimoNome);
            if (!sinonimoFormatado.equalsIgnoreCase(tag.getNome()) && !sinonimoRepository.existsByNomeAndTag(sinonimoFormatado, tag)) {
                Sinonimo sinonimo = new Sinonimo();
                sinonimo.setNome(sinonimoFormatado);
                sinonimo.setTag(tag);
                sinonimoRepository.save(sinonimo);
            }
        }
        return CompletableFuture.completedFuture(null);
    }


    public List<Tag> listarTagsOrdenadas() {
        return tagRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));}

    public Tag buscarTagPorId(Integer id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada."));}}
