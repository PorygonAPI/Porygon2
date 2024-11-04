package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Jornalista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JornalistaRepository extends JpaRepository<Jornalista, Integer> {
    Jornalista findByNome(String nome);
}