package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Sinonimo;
import edu.fatec.Porygon.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SinonimoRepository extends JpaRepository<Sinonimo, Long> {
    
    boolean existsByNome(String nome);
    void deleteByTag(Tag tag);
    List<Sinonimo> findByTag(Tag tag);
}
