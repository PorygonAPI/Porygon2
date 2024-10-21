package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.ApiDados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDadosRepository extends JpaRepository<ApiDados, Integer> {
    boolean existsByConteudo(String conteudo);
}