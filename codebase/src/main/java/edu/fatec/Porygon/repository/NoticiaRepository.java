package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Noticia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {
    boolean existsByHref(String href);

    @Query("Select n FROM Noticia n WHERE n.data between :dataStart and :dataEnd")
    List<Noticia> searchNewsByData(@Param("dataStart") LocalDate dataStart, @Param("dataEnd") LocalDate dataEnd);

    @Query("SELECT n FROM Noticia n LEFT JOIN FETCH n.tags WHERE n.id = :id")
    Optional<Noticia> findByIdWithTags(@Param("id") Integer id);
}