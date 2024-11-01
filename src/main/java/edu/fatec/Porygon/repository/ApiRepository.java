package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Api;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends JpaRepository<Api, Integer> {

    @Query("SELECT p FROM Api p LEFT JOIN FETCH p.tags WHERE p.id = :id")
    Optional<Api> findByIdWithTags(@Param("id") Integer id);

    boolean existsByNome(String nome);
    boolean existsByUrl(String url);
}

