package com.rfindapi.api_rfind.controller;

import com.rfindapi.api_rfind.Service.EmpresaService;
import model.Empresa;
import com.rfindapi.api_rfind.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiRfind/empresa")
public class EmpresaController {
    @Autowired
    EmpresaRepository empresaRepository;
    @Autowired
    private EmpresaService empresaService;

    @PostMapping(value="/cadastrar")
    public void insert(@RequestBody Empresa empresa){
        empresaService.insert(empresa);
    }

    @PutMapping(value="/atualizar")
    public void atualizar(@RequestBody Empresa empresa){
        empresaRepository.save(empresa);
    }

    @GetMapping(value="/listar")
    public List<Empresa> listar(){
        return empresaService.select();
    }

    @DeleteMapping(value = "/excluir/{cnpj}")
    public void excluirPorCnpj(@PathVariable String cnpj){
        empresaService.deleteByCnpj(cnpj);
    }

    @GetMapping(value = "/buscar/{cnpj}")
    public Empresa buscarPorCnpj(@PathVariable("cnpj") String cnpj){
        return empresaService.findByCnpj(cnpj);
    }

    @GetMapping(value = "/buscarPorEmail/{email}")
    public Empresa buscarPorEmail(@PathVariable("email") String email){
        return empresaService.findByEmail(email);
    }

}
