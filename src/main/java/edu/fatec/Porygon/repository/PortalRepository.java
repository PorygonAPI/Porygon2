package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Portal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortalRepository extends JpaRepository<Portal, Integer> {
}
