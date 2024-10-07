package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {
    boolean existsByHref(String href);
}
