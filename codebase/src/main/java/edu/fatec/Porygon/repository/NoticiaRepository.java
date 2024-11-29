package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Noticia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {
    boolean existsByHref(String href);

    @Query("Select n FROM Noticia n WHERE n.data between :dataStart and :dataEnd")
    List<Noticia> searchNewsByData(@Param("dataStart") LocalDate dataStart, @Param("dataEnd") LocalDate dataEnd);

    @Query("SELECT n FROM Noticia n LEFT JOIN FETCH n.tags WHERE n.id = :id")
    Optional<Noticia> findByIdWithTags(@Param("id") Integer id);

    @Query("SELECT DISTINCT n FROM Noticia n JOIN n.tags t WHERE t.id IN :tagIds")
    List<Noticia> findByTags(@Param("tagIds") List<Integer> tagIds);

    @Query("SELECT n FROM Noticia n JOIN n.tags t WHERE n.data BETWEEN :dataInicio AND :dataFim AND t.id IN :tagIds")
    List<Noticia> findByDataBetweenAndTagsIn(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, @Param("tagIds") List<Integer> tagIds);

    @Query("SELECT n FROM Noticia n JOIN n.tags t WHERE n.data BETWEEN :dataInicio AND :dataFim AND t.id IN :tagIds")
    Page<Noticia> findByDataBetweenAndTagsIn(@Param("dataInicio") LocalDate dataInicio,
                                             @Param("dataFim") LocalDate dataFim,
                                             @Param("tagIds") List<Integer> tagIds,
                                             Pageable pageable);

    @Query("SELECT DISTINCT n FROM Noticia n JOIN n.tags t WHERE t.id IN :tagIds")
    Page<Noticia> findByTags(@Param("tagIds") List<Integer> tagIds, Pageable pageable);

    @Query("SELECT n FROM Noticia n WHERE n.data BETWEEN :dataStart AND :dataEnd")
    Page<Noticia> searchNewsByData(@Param("dataStart") LocalDate dataStart,
                                   @Param("dataEnd") LocalDate dataEnd,
                                   Pageable pageable);

    Page<Noticia> findAll(Pageable pageable);
}
