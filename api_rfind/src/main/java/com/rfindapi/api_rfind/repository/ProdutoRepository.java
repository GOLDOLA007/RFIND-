package com.rfindapi.api_rfind.repository;

import jakarta.transaction.Transactional;
import model.Funcionario;
import model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto,Integer> {

    @Transactional
    Produto findById(int id);

    @Query("SELECT p FROM Produto p WHERE p.empresa.id = :empresaId AND p.ativado = true")
    List<Produto> findAtivosPorEmpresa(@Param("empresaId") int empresaId);

    @Query("SELECT p FROM Produto p WHERE p.empresa.id = :empresaId AND p.ativado = false")
    List<Produto> findNotAtivosPorEmpresa(@Param("empresaId") int empresaId);

}
