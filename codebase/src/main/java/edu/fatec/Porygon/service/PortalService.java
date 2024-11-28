package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Portal;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.PortalRepository;
import edu.fatec.Porygon.repository.TagRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class PortalService {

    @Autowired
    private PortalRepository portalRepository;

    @Autowired
    private DataScrapperService dataScrapperService;

    @Autowired
    private TagRepository tagRepository;

    public List<Portal> listarTodos() {
        return portalRepository.findAll();
    }

    public Optional<Portal> buscarPorId(Integer id) {
        return portalRepository.findByIdWithTags(id);
    }

    public Portal salvar(Portal portal, List<Integer> tagIds) {
        portal.setDataCriacao(LocalDate.now());
        
        if (tagIds != null && !tagIds.isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
            portal.setTags(tags);
        }

        return portalRepository.save(portal);
    }

    public Portal salvar(Portal portal) {
        return salvar(portal, null);
    }

    public void deletar(Integer id) {
        Portal portal = portalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Portal não encontrado"));
        
        portal.setTags(new HashSet<>());
        portalRepository.save(portal);
        
        portalRepository.deleteById(id);
    }

    public Portal atualizar(Portal portal, List<Integer> tagIds) {
        Portal portalExistente = portalRepository.findById(portal.getId())
            .orElseThrow(() -> new RuntimeException("Portal não encontrado"));
    
        portal.setDataCriacao(portalExistente.getDataCriacao());
        
        portal.setUltimaAtualizacao(portalExistente.getUltimaAtualizacao());
        portal.setHasScrapedToday(portalExistente.isHasScrapedToday());
    
        if (tagIds != null) {
            Set<Tag> novasTags = tagIds.isEmpty() 
                ? new HashSet<>() 
                : new HashSet<>(tagRepository.findAllById(tagIds));
            portal.setTags(novasTags);
        }
    
        return portalRepository.save(portal);
    }

    public Portal atualizar(Portal portal) {
        return atualizar(portal, null);
    }

    @Transactional
    public CompletableFuture<Portal> alterarStatus(Integer id, boolean novoStatus) {
        Optional<Portal> portalOptional = portalRepository.findById(id);
        
        if (portalOptional.isPresent()) {
            Portal portal = portalOptional.get();
            portal.setAtivo(novoStatus);
            
            if (novoStatus && !portal.isHasScrapedToday()) {
                dataScrapperService.scrapeDatabyPortalID(portal.getId());
                portal.setHasScrapedToday(true);
                portal.setUltimaAtualizacao(LocalDate.now());
            }
            
            return CompletableFuture.completedFuture(portalRepository.save(portal));
        }
        return CompletableFuture.completedFuture(null); 
    }
    

    public Portal atualizarTags(Integer portalId, List<Integer> tagIds) {
        Portal portal = portalRepository.findById(portalId)
            .orElseThrow(() -> new RuntimeException("Portal não encontrado"));

        if (tagIds != null) {
            Set<Tag> novasTags = tagIds.isEmpty() 
                ? new HashSet<>() 
                : new HashSet<>(tagRepository.findAllById(tagIds));
            portal.setTags(novasTags);
        }

        return portalRepository.save(portal);
    }

     public List<String> getSeletoresTitulo() {
        return portalRepository.findDistinctSeletorTitulo();
    }

    public List<String> getSeletoresJornalista() {
        return portalRepository.findDistinctSeletorJornalista();
    }

    public List<String> getSeletoresConteudo() {
        return portalRepository.findDistinctSeletorConteudo();
    }

    public List<String> getSeletoresDataPublicacao() {
        return portalRepository.findDistinctSeletorDataPublicacao();
    }

    public List<String> getSeletoresCaminhoNoticia() {
        return portalRepository.findDistinctSeletorCaminhoNoticia();
    }
}