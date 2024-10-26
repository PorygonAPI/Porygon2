package edu.fatec.Porygon.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagScrapperService {

    public static List<String> buscarSinonimos(String palavra) {
        List<String> sinonimos = buscarSinonimosDicio(palavra);
        if (sinonimos == null || sinonimos.isEmpty()) {
            sinonimos = buscarSinonimosSinonimos(palavra);
        }
        return sinonimos;
    }

    private static List<String> buscarSinonimosDicio(String palavra) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> buscarSinonimosSinonimos(String palavra) {
        try {
            String url = "https://www.sinonimos.com.br/" + palavra + "/";
            Document doc = Jsoup.connect(url).get();
            Elements sinonimos = doc.select("a.sinonimo");

            if (!sinonimos.isEmpty()) {
                return sinonimos.stream()
                        .map(element -> element.text().trim())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
