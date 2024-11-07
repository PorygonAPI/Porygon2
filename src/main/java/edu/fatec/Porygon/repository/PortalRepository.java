package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.Portal;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PortalRepository extends JpaRepository<Portal, Integer> {

    @Query("SELECT p FROM Portal p LEFT JOIN FETCH p.tags WHERE p.id = :id")
    Optional<Portal> findByIdWithTags(@Param("id") Integer id);

     @Query("SELECT DISTINCT p.seletorTitulo FROM Portal p WHERE p.seletorTitulo IS NOT NULL")
    List<String> findDistinctSeletorTitulo();

    @Query("SELECT DISTINCT p.seletorJornalista FROM Portal p WHERE p.seletorJornalista IS NOT NULL")
    List<String> findDistinctSeletorJornalista();

    @Query("SELECT DISTINCT p.seletorConteudo FROM Portal p WHERE p.seletorConteudo IS NOT NULL")
    List<String> findDistinctSeletorConteudo();

    @Query("SELECT DISTINCT p.seletorDataPublicacao FROM Portal p WHERE p.seletorDataPublicacao IS NOT NULL")
    List<String> findDistinctSeletorDataPublicacao();

    @Query("SELECT DISTINCT p.seletorCaminhoNoticia FROM Portal p WHERE p.seletorCaminhoNoticia IS NOT NULL")
    List<String> findDistinctSeletorCaminhoNoticia();

    boolean existsByUrl(String url);
    boolean existsByNome(String nome);
}