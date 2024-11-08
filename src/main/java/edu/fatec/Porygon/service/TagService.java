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
        // Verifica se a tag já existe no banco de dados
        if (tagRepository.existsByNome(tag.getNome())) {
            throw new RuntimeException("A tag já existe.");
        }

        // Verifica se a tag já existe como sinônimo
        List<Sinonimo> sinonimosExistentes = sinonimoRepository.findByTag(tag.getNome());
        if (!sinonimosExistentes.isEmpty()) {
            // Aqui você pode disparar um modal ou algo similar para pedir confirmação ao usuário
            // Para simular isso, podemos lançar uma exceção informando sobre a situação
            throw new RuntimeException("A tag já existe como sinônimo de outra tag. Deseja continuar?");
        }

        // Formatar o nome da tag
        tag.setNome(formatarPalavra(tag.getNome()));

        // Salva a nova tag
        Tag novaTag = tagRepository.save(tag);

        // Atualiza os sinônimos associados à tag
        atualizarSinonimos(novaTag.getNome().toLowerCase(), novaTag);
        return novaTag;
    }

    // Método para editar uma tag, caso seja necessário
    public Tag editarTag(Integer id, String novoNome) {
        Tag tagExistente = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada."));

        // Verifica se o novo nome já existe no banco
        if (!tagExistente.getNome().equalsIgnoreCase(novoNome) && tagRepository.existsByNome(novoNome)) {
            throw new RuntimeException("Uma tag com este nome já existe.");
        }

        // Formatar o nome da nova tag
        formatarPalavra(novoNome);
        tagExistente.setNome(formatarPalavra(novoNome));
        tagRepository.save(tagExistente);

        // Atualiza os sinônimos associados
        atualizarSinonimos(tagExistente.getNome().toLowerCase(), tagExistente);
        return tagExistente;
    }

    // Método de formatação do nome da tag
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

    // Atualiza os sinônimos de uma tag
    private void atualizarSinonimos(String nomeTagParaScraping, Tag tag) {
        List<String> sinonimos = tagScrapperService.buscarSinonimos(nomeTagParaScraping);
        vincularSinonimos(sinonimos, tag);
    }

    // Vincula os sinônimos à tag
    private void vincularSinonimos(List<String> sinonimos, Tag tag) {
        List<Sinonimo> sinonimosAntigos = sinonimoRepository.findByTag(tag);
        for (Sinonimo sinonimoAntigo : sinonimosAntigos) {
            if (!sinonimos.contains(sinonimoAntigo.getNome())) {
                sinonimoRepository.delete(sinonimoAntigo);
            }
        }

        for (String sinonimoNome : sinonimos) {
            String sinonimoFormatado = formatarPalavra(sinonimoNome);
            if (!sinonimoRepository.existsByNomeAndTag(sinonimoFormatado, tag)) {
                Sinonimo sinonimo = new Sinonimo();
                sinonimo.setNome(sinonimoFormatado);
                sinonimo.setTag(tag);
                sinonimoRepository.save(sinonimo);
            }
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
