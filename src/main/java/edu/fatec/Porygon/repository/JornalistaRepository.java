package edu.fatec.Porygon.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.fatec.Porygon.model.Jornalista;

public interface JornalistaRepository extends JpaRepository<Jornalista, Integer> {
    Optional<Jornalista> findByNome(String nomeAutor);
}