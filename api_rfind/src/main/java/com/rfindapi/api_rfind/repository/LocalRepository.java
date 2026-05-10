package com.rfindapi.api_rfind.repository;

import jakarta.transaction.Transactional;
import model.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocalRepository extends JpaRepository<Local,Integer> {
    @Transactional
    Local findById(int id);

    @Query("SELECT l FROM Local l WHERE l.empresa.id = :empresaId")
    List<Local> findPorEmpresa(@Param("empresaId") int empresaId);

    @Query("SELECT r FROM Local r WHERE r.idrfid = :idrfid")
    Optional<Local> findByIdrfid(@Param("idrfid") String idrfid);

}
