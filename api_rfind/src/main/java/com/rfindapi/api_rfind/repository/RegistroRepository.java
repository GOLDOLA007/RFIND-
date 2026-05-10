package com.rfindapi.api_rfind.repository;

import jakarta.transaction.Transactional;
import model.Local;
import model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RegistroRepository extends JpaRepository<Registro,Integer> {
    @Transactional
    Optional<Registro> findById(int id);

    @Query("SELECT r FROM Registro r WHERE r.empresa.id = :empresaId")
    List<Registro> findPorEmpresa(@Param("empresaId") int empresaId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Registro r WHERE r.empresa.id = :empresaId")
    void deleteAllByEmpresaId(@Param("empresaId") Integer empresaId);

    @Query("SELECT r FROM Registro r WHERE r.funcionario.id = :funcionarioId " +
            "AND r.local = :local AND r.dataSaida IS NULL")
    Optional<Registro> findRegistroAberto(@Param("funcionarioId") Integer funcionario,
                                          @Param("local") Local local);

    @Transactional
    @Modifying
    @Query("UPDATE Registro r SET r.dataSaida = :dataSaida, r.horaSaida = :horaSaida " +
            "WHERE r.id = :id")
    void registrarSaida(@Param("id") Integer id,
                        @Param("dataSaida") LocalDate dataSaida,
                        @Param("horaSaida") LocalTime horaSaida);

}
