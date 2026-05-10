package com.rfindapi.api_rfind.repository;

import model.Empresa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa,Integer> {
    @Transactional
    void deleteByCnpj(String cnpj);

    Empresa findByCnpj(String cnpj);

    Empresa findById(int id);

    Optional <Empresa> findByEmail(String email);
}
