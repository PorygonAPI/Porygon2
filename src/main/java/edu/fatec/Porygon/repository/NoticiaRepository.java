package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Noticia;
// import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {
    boolean existsByHref(String href);

    // List<Noticia> acharDataEntre(java.util.Date dataInicio, java.util.Date dataFim); // Método de filtro de consulta: data}
}
