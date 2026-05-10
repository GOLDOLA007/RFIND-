package com.rfindapi.api_rfind.controller;

import com.rfindapi.api_rfind.Service.FuncionarioService;
import com.rfindapi.api_rfind.Service.LocalService;
import model.Funcionario;
import model.Local;
import com.rfindapi.api_rfind.repository.LocalRepository;
import model.dto.FuncionarioUpdateDTO;
import model.dto.LocalUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiRfind/local")
public class LocalController {
    @Autowired
    LocalRepository localRepository;
    @Autowired
    LocalService localService;

    @PostMapping(value = "/cadastrar")
    public void cadastrar(@RequestBody Local local){
        localService.insert(local);
    }

    @GetMapping(value = "/buscar/empresa/{empresaId}")
    public List<Local> buscarPorEmpresa(@PathVariable("empresaId") int empresaId){
        return localService.buscarporEmpresa(empresaId);
    }


    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Local> atualizarLocal(@PathVariable("id") int id,
                                                            @RequestBody LocalUpdateDTO dto) {
        try {
            Local atualizado = localService.update(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/excluir/{id}/{empresaId}")
    public ResponseEntity<Void> excluirLocal(
            @PathVariable("id") int id,
            @PathVariable("empresaId") int empresaId) {

        localService.excluirLocal(id, empresaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/buscar/localPesquisa")
    public List<Local> buscarLocais(
            @RequestParam(name="id",required = false) Integer id,
            @RequestParam(name="nome",required = false) String nome,
            @RequestParam(name="descricao",required = false) String descricao,
            @RequestParam(name="sensor",required = false) String sensor,
            @RequestParam (name="empresaId")Integer empresaId
    )
    {
        return localService.buscarComFiltros(id, nome, descricao, sensor, empresaId);
    }

    @GetMapping(value = "/listar")
    public List<Local> listar(){
        return localRepository.findAll();
    }
    @DeleteMapping(value = "/excluir/{id}")
    public void deleteById(@PathVariable int id){
        localRepository.deleteById(id);
    }
}
