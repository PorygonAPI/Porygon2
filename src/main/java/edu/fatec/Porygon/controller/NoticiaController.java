package edu.fatec.Porygon.controller;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import edu.fatec.Porygon.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Controller
public class NoticiaController {

    @Autowired
    private NoticiaRepository noticiaRepository;
    @Autowired
    private NoticiaService noticiaService;

    // private static final Logger logger = (Logger) LoggerFactory.getLogger(NoticiaController.class);
    @GetMapping("/")
    public String redirecionarParaIndex() {
    return "redirect:/index";
    }

    @GetMapping("/index")
    public String listarNoticias(@RequestParam(defaultValue = "0") int page, Model model) {
    int pageSize = 5; // Tamanho da página
    Page<Noticia> noticiasPage = noticiaRepository.findAll(PageRequest.of(page, pageSize));

    model.addAttribute("noticias", noticiasPage.getContent());
    model.addAttribute("paginaAtual", page + 1); // Para mostrar a página de forma amigável (1-indexed)
    model.addAttribute("paginaAnterior", page - 1); // Para mostrar a página de forma amigável (1-indexed)
    model.addAttribute("totalPaginas", noticiasPage.getTotalPages());

    return "index";
    }

    // Método para buscar o detalhe de uma notícia por ID
    @GetMapping("/noticias/detalhe/{id}")
    @ResponseBody
    public ResponseEntity<Noticia> detalheNoticiaJson(@PathVariable Integer id) {
        Optional<Noticia> noticiaOptional = noticiaService.findById(id);
        return noticiaOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // @GetMapping("/index")
    // public String listarNoticias(Model model) {
    //     List<Noticia> noticias = noticiaRepository.findAll();
    //     model.addAttribute("noticias", noticias);
    //     return "index";
    // }
    // @GetMapping("/noticias/detalhe/{id}")
    // @ResponseBody
    // public ResponseEntity<Noticia> detalheNoticiaJson(@PathVariable Integer id) {
    //     Optional<Noticia> noticiaOptional = noticiaRepository.findById(id);
    //     return noticiaOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    // }

     @GetMapping("/noticias")
     @ResponseBody
     public List<Noticia> listarNoticiasPorData(
             @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
             @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

         return noticiaService.listarNoticiasPorData(dataInicio, dataFim);
     }
}


