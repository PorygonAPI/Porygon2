package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Jornalista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JornalistaRepository extends JpaRepository<Jornalista, Long> {
    Optional<Jornalista> findByNome(String nome);
}
