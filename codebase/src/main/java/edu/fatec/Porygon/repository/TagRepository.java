package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    boolean existsByNome(String nome);
    Optional<Tag> findByNome(String nome);

    List<Tag> findAllByPortais_Id(Integer portalId);
}