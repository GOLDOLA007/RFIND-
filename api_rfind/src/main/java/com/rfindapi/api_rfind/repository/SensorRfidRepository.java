package com.rfindapi.api_rfind.repository;

import model.Empresa;
import model.Local;
import model.Registro;
import model.SensorRfid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SensorRfidRepository extends JpaRepository<SensorRfid, Integer> {
    Optional<SensorRfid> findByNomeAndEmpresaId(String nome, Integer empresaId);

    Optional<SensorRfid> findByNomeAndEmpresa(String nome, Empresa empresa);
    boolean existsByNomeAndEmpresaId(String nome, int empresaId);

    @Query("SELECT s FROM SensorRfid s WHERE s.empresa.id = :empresaId")
    List<SensorRfid> findPorEmpresa(@Param("empresaId") int empresaId);

    @Query("SELECT s FROM SensorRfid s WHERE s.empresa.id = :empresaId AND NOT EXISTS " +
            "(SELECT l FROM Local l WHERE l.idrfid = s.nome)")
    List<SensorRfid> findSensoresNaoVinculados(@Param("empresaId") int empresaId);

    @Query("SELECT r FROM Registro r WHERE r.funcionario.id = :funcionarioId " +
            "AND r.local = :local AND r.dataSaida IS NULL")
    Optional<Registro> findRegistroAberto(@Param("funcionarioId") Integer funcionarioId,
                                          @Param("local") Local local);
}