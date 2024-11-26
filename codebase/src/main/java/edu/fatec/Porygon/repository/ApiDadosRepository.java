package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.ApiDados;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDadosRepository extends JpaRepository<ApiDados, Integer> {
    boolean existsByConteudo(String conteudo);
    Optional<ApiDados> findById(Long id);
    List<ApiDados> findByApiId(Integer apiId);
    List<ApiDados> findDistinctByTags_IdIn(List<Integer> tagIds);
    @Query("SELECT ad FROM ApiDados ad JOIN ad.tags t WHERE ad.dataColeta BETWEEN :dataInicio AND :dataFim")
    List<ApiDados> findByDataColetaBetweenAndTags_IdIn(LocalDate dataInicio, LocalDate dataFim);
}