package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.ApiDados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApiDadosRepository extends JpaRepository<ApiDados, Integer> {
    boolean existsByConteudo(String conteudo);
    List<ApiDados> findByApiId(Integer apiId);
}