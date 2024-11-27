package edu.fatec.Porygon.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class TagScrapperService {

    @Async
    public CompletableFuture<List<String>> buscarSinonimosAsync(String palavra) {
        CompletableFuture<List<String>> sinonimosDicioFuture = CompletableFuture
                .supplyAsync(() -> buscarSinonimosDeFonte(palavra, "dicio"));
        CompletableFuture<List<String>> sinonimosSinonimosFuture = CompletableFuture
                .supplyAsync(() -> buscarSinonimosDeFonte(palavra, "sinonimos"));

        return sinonimosDicioFuture.thenCombine(sinonimosSinonimosFuture, (sinonimosDicio, sinonimosSinonimos) -> {
            try {
                return Stream.concat(sinonimosDicio.stream(), sinonimosSinonimos.stream())
                        .distinct()
                        .collect(Collectors.toList());
            } catch (Exception e) {
                return Collections.emptyList();
            }
        });
    }

    private List<String> buscarSinonimosDeFonte(String palavra, String fonte) {
        String url;
        Elements sinonimos = null;

        try {
            if ("dicio".equals(fonte)) {
                url = "https://www.dicio.com.br/" + palavra + "/";
                Document doc = Jsoup.connect(url).get();
                sinonimos = doc.select("p.sinonimos");
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
            } else if ("sinonimos".equals(fonte)) {
                url = "https://www.sinonimos.com.br/" + palavra + "/";
                Document doc = Jsoup.connect(url).get();
                sinonimos = doc.select("a.sinonimo");
                if (!sinonimos.isEmpty()) {
                    return sinonimos.stream()
                            .map(element -> element.text().trim())
                            .collect(Collectors.toList());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao buscar sinonimos de " + palavra + " na fonte ");
        }
        return Collections.emptyList();
    }
}
