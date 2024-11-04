package edu.fatec.Porygon.repository;

import edu.fatec.Porygon.model.ApiDados;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDadosRepository extends JpaRepository<ApiDados, Integer> {
    boolean existsByConteudo(String conteudo);
    Optional<ApiDados> findById(Long id);
    Page<ApiDados> findAll(Pageable pageable);
}

