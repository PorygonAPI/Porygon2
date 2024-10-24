package edu.fatec.Porygon;

import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class PorygonApplicationTests {

    @Autowired
    private TagService tagService;

    @Test
    void contextLoads() {
    }

    @Test
    void testCriarTagComSinonimos() {
        Tag novaTag = new Tag();
        novaTag.setNome("macaxeira");

        Tag tagSalva = tagService.criarTag(novaTag);
        assertNotNull(tagSalva.getId(), "A tag não foi salva.");
        List<Sinonimo> sinonimos = tagSalva.getSinonimos();

        assertNotNull(sinonimos, "A lista de sinônimos não foi preenchida.");
        assertFalse(sinonimos.isEmpty(), "Nenhum sinônimo foi salvo.");

        List<String> nomesSinonimos = sinonimos.stream()
                                               .map(Sinonimo::getNome)
                                               .collect(Collectors.toList());

        assertTrue(nomesSinonimos.contains("mandioca"), "Sinônimo esperado 'mandioca' não encontrado.");
        assertTrue(nomesSinonimos.contains("aipim"), "Sinônimo esperado 'aipim' não encontrado.");
    }
}
