package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Portal;
import edu.fatec.Porygon.repository.PortalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PortalService {

    @Autowired
    private PortalRepository portalRepository;

    public List<Portal> listarTodos() {
        return portalRepository.findAll();
    }

    public Optional<Portal> buscarPorId(Integer id) {
        return portalRepository.findById(id);
    }

    public Portal salvar(Portal portal) {
        portal.setDataCriacao(LocalDate.now());
        return portalRepository.save(portal);
    }

    public void deletar(Integer id) {
        portalRepository.deleteById(id);
    }

    public Portal atualizar(Portal portal) {
        return portalRepository.save(portal);
    }

    public Portal alterarStatus(Integer id, boolean novoStatus) {
        Optional<Portal> portalOptional = portalRepository.findById(id);
        if (portalOptional.isPresent()) {
            Portal portal = portalOptional.get();
            portal.setAtivo(novoStatus); 
            return portalRepository.save(portal); 
        }
        return null; 
    }
}