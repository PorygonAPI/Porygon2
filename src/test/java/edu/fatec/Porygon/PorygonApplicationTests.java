package edu.fatec.Porygon;

import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
    }

    @Test
    void testValidarNomeTag() {
        Tag tagValida = new Tag();
        tagValida.setNome("palavra-composta");
        assertTrue(tagValida.getNome().length() < 46, "A tag deve ter menos de 46 caracteres.");
        assertTrue(tagValida.getNome().contains("-"), "A tag deve ser composta e conter hífen.");

        Tag tagInvalidaSemHifen = new Tag();
        tagInvalidaSemHifen.setNome("palavracomposta");
        assertFalse(tagInvalidaSemHifen.getNome().contains("-"), "A tag não deve ser composta.");

        Tag tagInvalidaComprida = new Tag();
        tagInvalidaComprida.setNome("essa-tag-excede-o-limite-de-quarenta-e-seis-caracteres-e-deve-falhar");
        assertFalse(tagInvalidaComprida.getNome().length() < 46, "A tag deve ter menos de 46 caracteres.");
    }
}
