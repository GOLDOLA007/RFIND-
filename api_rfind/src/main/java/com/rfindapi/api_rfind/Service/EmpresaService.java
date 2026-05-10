package com.rfindapi.api_rfind.Service;

import model.Empresa;
import com.rfindapi.api_rfind.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public Empresa insert(Empresa empresa) {
        if(empresaRepository.findByCnpj(empresa.getCnpj()) != null) {
            throw new RuntimeException("Empresa com esse CNPJ já existe!");
        }
        return empresaRepository.save(empresa);
    }

    public List<Empresa> select(){
        return empresaRepository.findAll();
    }

    public Empresa findByCnpj(String cnpj) {
        Empresa empresa = empresaRepository.findByCnpj(cnpj);
        if(empresa == null) {
            throw new RuntimeException("CNPJ não encontrado");
        }
        return empresa;
    }

    public Empresa findByEmail(String email) {
        return empresaRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email não encontrado"));
    }

    public void deleteByCnpj(String cnpj) {
        Empresa empresa = empresaRepository.findByCnpj(cnpj);

        if(cnpj == null){
            throw new RuntimeException("CNPJ não encontrado para exclusão!");
        }
        empresaRepository.deleteByCnpj(cnpj);
    }

}
