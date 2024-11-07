package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Portal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PortalRepository extends JpaRepository<Portal, Integer> {

    @Query("SELECT p FROM Portal p LEFT JOIN FETCH p.tags WHERE p.id = :id")
    Optional<Portal> findByIdWithTags(@Param("id") Integer id);

    boolean existsByUrl(String url);
    boolean existsByNome(String nome);
}