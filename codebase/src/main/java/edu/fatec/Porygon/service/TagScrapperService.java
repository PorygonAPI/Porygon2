package edu.fatec.Porygon.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TagScrapperService {

    public List<String> buscarSinonimos(String palavra) {
        List<String> sinonimosDicio = buscarSinonimosDicio(palavra);
        List<String> sinonimosSinonimos = buscarSinonimosSinonimos(palavra);
        return Stream.concat(sinonimosDicio.stream(), sinonimosSinonimos.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> buscarSinonimosDicio(String palavra) {
        try {
            String url = "https://www.dicio.com.br/" + palavra + "/";
            Document doc = Jsoup.connect(url).get();
            Elements sinonimos = doc.select("p.sinonimos");

            if (!sinonimos.isEmpty()) {
                String textoCompleto = sinonimos.first().text();
                int index = textoCompleto.indexOf("sinônimo de:");
                if (index != -1) {
                    String sinonimosTexto = textoCompleto.substring(index + "sinônimo de:".length()).trim();
                    return Arrays.stream(sinonimosTexto.split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao buscar sinônimos no Dicio: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    private List<String> buscarSinonimosSinonimos(String palavra) {
        try {
            String url = "https://www.sinonimos.com.br/" + palavra + "/";
            Document doc = Jsoup.connect(url).get();
            Elements sinonimos = doc.select("a.sinonimo");

            if (!sinonimos.isEmpty()) {
                return sinonimos.stream()
                        .map(element -> element.text().trim())
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            System.err.println("Erro ao buscar sinônimos no Sinonimos" + e.getMessage());
        }
        return Collections.emptyList();
    }
}
