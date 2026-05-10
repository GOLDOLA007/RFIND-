package com.rfindapi.api_rfind.controller;

import com.rfindapi.api_rfind.Service.FuncionarioService;
import model.dto.FuncionarioUpdateDTO;
import model.Funcionario;
import com.rfindapi.api_rfind.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiRfind/funcionario")
public class FuncionarioController {
    @Autowired
    FuncionarioRepository funcionarioRepository;
    @Autowired
    FuncionarioService funcionarioService;

    @PostMapping(value="/cadastrar")
    public void cadastrar(@RequestBody Funcionario funcionario){
        funcionarioService.insert(funcionario);
    }

    @DeleteMapping("/excluir/{id}/{empresaId}")
    public ResponseEntity<Void> excluirFuncionario(
            @PathVariable("id") int id,
            @PathVariable("empresaId") int empresaId) {

        funcionarioService.excluirFuncionario(id, empresaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/buscar/{id}")
    public Funcionario buscarPorId(@PathVariable("id") int id){
        return funcionarioRepository.findById(id);
    }

    @GetMapping(value = "/buscar/ativos/empresa/{empresaId}")
    public List<Funcionario> buscarAtivosPorEmpresa(@PathVariable("empresaId") int empresaId){
        return funcionarioService.buscarAtivosporEmpresa(empresaId);
    }

    @GetMapping(value = "/buscar/desativados/empresa/{empresaId}")
    public List<Funcionario> buscarDesativadosPorEmpresa(@PathVariable("empresaId") int empresaId){
        return funcionarioService.buscarDesativadosPorEmpresa(empresaId);
    }

    @GetMapping(value = "/listar")
    public List<Funcionario> listar(){
        return funcionarioRepository.findAll();
    }


    @GetMapping(value = "/buscar/funcPesquisa")
    public List<Funcionario> buscarFuncionarios(
            @RequestParam(name="id",required = false) Integer id,
            @RequestParam(name="nome",required = false) String nome,
            @RequestParam(name="sobrenome",required = false) String sobrenome,
            @RequestParam(name="setor",required = false) String setor,
            @RequestParam(name="tag",required = false) String tag,
            @RequestParam (name="empresaId")Integer empresaId
            )
    {
        return funcionarioService.buscarComFiltros(id, nome, sobrenome, setor, tag, empresaId);
    }


    /*
    @PutMapping(value = "/atualizar")
    public void atualizar(@RequestBody Funcionario funcionario){
        funcionarioRepository.save(funcionario);
    }*/

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Funcionario> atualizarFuncionario(@PathVariable("id") int id,
                                                            @RequestBody FuncionarioUpdateDTO dto) {
        try {
            Funcionario atualizado = funcionarioService.update(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/desativar/{id}")
    public ResponseEntity<String> desativarFuncionario(
            @PathVariable("id") Integer id,
            @RequestParam("empresaId") Integer empresaId) {
        try {
            funcionarioService.desativar(id, empresaId);
            return ResponseEntity.ok("Funcionário desativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/ativar/{id}")
    public ResponseEntity<String> ativarFuncionario(
            @PathVariable("id") Integer id,
            @RequestParam("empresaId") Integer empresaId
    ){
        try {
            funcionarioService.ativar(id, empresaId);
            return ResponseEntity.ok("Funcionário ativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



}
