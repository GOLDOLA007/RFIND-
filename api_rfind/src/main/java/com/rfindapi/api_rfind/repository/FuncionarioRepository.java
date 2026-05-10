package com.rfindapi.api_rfind.repository;

import jakarta.transaction.Transactional;
import model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario,Integer> {

    @Transactional
    Funcionario findById(int id);

    @Query("SELECT f FROM Funcionario f WHERE f.empresa.id = :empresaId AND f.ativado = true")
    List<Funcionario> findAtivosPorEmpresa(@Param("empresaId") int empresaId);

    @Query("SELECT f FROM Funcionario  f WHERE f.empresa.id = :empresaId AND f.ativado = false")
    List<Funcionario> findNotAtivosPorEmpresa(@Param("empresaId") int empresaId);

    // Método para verificar se existe funcionário com a tag RFID
    boolean existsByTagRfid(String tagRfid);

    @Query("SELECT f FROM Funcionario f WHERE TRIM(f.tagRfid) = TRIM(:tag)")
    Optional<Funcionario> findByTagRfid(@Param("tag") String tag);

    @Query("SELECT TRIM(f.tagRfid) FROM Funcionario f WHERE f.tagRfid IS NOT NULL")
    List<String> findAllTagsRfid();

}
