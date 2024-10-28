package edu.fatec.Porygon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.repository.TagRepository;
import edu.fatec.Porygon.repository.SinonimoRepository;

import java.util.List;

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
            throw new RuntimeException("A tag já existe.");}

        validarPalavra(tag.getNome());
        tag.setNome(formatarPalavra(tag.getNome()));
        Tag novaTag = tagRepository.save(tag);
        atualizarSinonimos(novaTag.getNome().toLowerCase(), novaTag);
        return novaTag;}

    public Tag editarTag(Integer id, String novoNome) {
        Tag tagExistente = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada."));

        if (!tagExistente.getNome().equalsIgnoreCase(novoNome) && tagRepository.existsByNome(novoNome)) {
            throw new RuntimeException("Uma tag com este nome já existe.");}

        validarPalavra(novoNome);
        tagExistente.setNome(formatarPalavra(novoNome));
        tagRepository.save(tagExistente);
        atualizarSinonimos(tagExistente.getNome().toLowerCase(), tagExistente);
        return tagExistente;}

    private String formatarPalavra(String nome) {
        return nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();}
    private void validarPalavra(String palavra) {
        String regex = "^[A-Za-zÀ-ÖØ-öø-ÿ]+([ -][A-Za-zÀ-ÖØ-öø-ÿ]+)*$";
        if (!palavra.matches(regex)) {
            throw new IllegalArgumentException("Cadastre 1 (uma) palavra por vez ou apenas palavras com hífen ou espaço.");}}

    private void atualizarSinonimos(String nomeTagParaScraping, Tag tag) {
        List<String> sinonimos = tagScrapperService.buscarSinonimos(nomeTagParaScraping);
        vincularSinonimos(sinonimos, tag);}

    private void vincularSinonimos(List<String> sinonimos, Tag tag) {
        List<Sinonimo> sinonimosAntigos = sinonimoRepository.findByTag(tag);
        for (Sinonimo sinonimoAntigo : sinonimosAntigos) {
            if (!sinonimos.contains(sinonimoAntigo.getNome())) {
                sinonimoRepository.delete(sinonimoAntigo);}}

        for (String sinonimoNome : sinonimos) {
            if (!sinonimoRepository.existsByNomeAndTag(sinonimoNome, tag)) {
                Sinonimo sinonimo = new Sinonimo();
                sinonimo.setNome(sinonimoNome);
                sinonimo.setTag(tag);
                sinonimoRepository.save(sinonimo);}}}

    public List<Tag> listarTagsOrdenadas() {
        return tagRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));}

    public Tag buscarTagPorId(Integer id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada."));}}