package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Noticia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {
    boolean existsByHref(String href);

    @Query("SELECT n FROM Noticia n WHERE n.data BETWEEN :dataStart AND :dataEnd")
    Page<Noticia> searchNewsByData(@Param("dataStart") LocalDate dataStart, 
                                   @Param("dataEnd") LocalDate dataEnd, 
                                   Pageable pageable);
}