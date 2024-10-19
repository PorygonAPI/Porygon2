package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<Api, Integer> {

    boolean existsByNome(String nome);
    boolean existsByUrl(String url);
}

