package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.ApiDados;
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
public interface ApiDadosRepository extends JpaRepository<ApiDados, Integer> {
    boolean existsByConteudo(String conteudo);
    Optional<ApiDados> findById(Long id);
    List<ApiDados> findDistinctByTags_IdIn(List<Integer> tagIds);
    List<ApiDados> findAllByOrderByIdDesc();
    @Query("SELECT DISTINCT ad FROM ApiDados ad JOIN ad.tags t " + "WHERE ad.dataColeta BETWEEN :dataInicio AND :dataFim " + "AND t.id IN :tagIds")
    List<ApiDados> findDistinctByDataColetaBetweenAndTags_IdIn(
            LocalDate dataInicio,
            LocalDate dataFim,
            List<Integer> tagIds
    );
    List<ApiDados> findByDataColetaBetween(
            LocalDate dataInicio,
            LocalDate dataFim
    );

    @Query("SELECT DISTINCT ad FROM ApiDados ad JOIN ad.tags t " +
           "WHERE ad.dataColeta BETWEEN :dataInicio AND :dataFim " +
           "AND t.id IN :tagIds")
    Page<ApiDados> findDistinctByDataColetaBetweenAndTags_IdIn(
            LocalDate dataInicio,
            LocalDate dataFim,
            List<Integer> tagIds,
            Pageable pageable);

    Page<ApiDados> findByDataColetaBetween(
            LocalDate dataInicio,
            LocalDate dataFim,
            Pageable pageable);

    Page<ApiDados> findDistinctByTags_IdIn(
            List<Integer> tagIds,
            Pageable pageable);

    Page<ApiDados> findAll(Pageable pageable);
}
