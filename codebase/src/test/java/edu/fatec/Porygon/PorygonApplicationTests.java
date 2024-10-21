package edu.fatec.Porygon;

import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

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
        assertNotNull(tagSalva.getNomeSinonimo(), "Sinônimos não foram preenchidos.");
        assertTrue(tagSalva.getNomeSinonimo().contains("mandioca"), "Sinônimo esperado 'mandioca' não encontrado.");
        assertTrue(tagSalva.getNomeSinonimo().contains("aipim"), "Sinônimo esperado 'aipim' não encontrado.");

        System.out.println("Tag criada com sinônimos: " + tagSalva.getNomeSinonimo());
    }
}
