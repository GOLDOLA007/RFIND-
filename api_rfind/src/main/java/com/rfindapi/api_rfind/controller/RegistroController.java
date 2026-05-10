package com.rfindapi.api_rfind.controller;

import com.rfindapi.api_rfind.Service.RegistroService;
import model.Produto;
import model.Registro;
import com.rfindapi.api_rfind.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/apiRfind/registro")
public class RegistroController {

    @Autowired
    RegistroService reistroService;
    @Autowired
    private RegistroService registroService;

    @PostMapping(value="/cadastrar")
    public void cadastrar(@RequestBody Registro registro){
        registroService.insert(registro);
    }

    @GetMapping(value = "/buscar/{empresaId}")
    public List<Registro> buscarPorEmpresa(@PathVariable("empresaId") int empresaId){
        try {
            return registroService.buscarporEmpresa(empresaId);
        } catch (Exception e) {
            e.printStackTrace(); // Mostra no console o erro exato
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar registros", e);
        }
    }



    @GetMapping("/buscar/regPesquisa")
    public ResponseEntity<List<Registro>> buscarRegistros(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "funcionario", required = false) String funcionario,
            @RequestParam(name = "local", required = false) String local,
            @RequestParam(name = "sensor", required = false) String sensor,
            @RequestParam(name = "dataEntrada", required = false) String dataEntrada,
            @RequestParam(name = "horarioEntrada", required = false) String horarioEntrada,
            @RequestParam(name = "dataSaida", required = false) String dataSaida,
            @RequestParam(name = "horarioSaida", required = false) String horarioSaida,
            @RequestParam(name = "empresaId") Integer empresaId) {

        try {

            List<Registro> registros = registroService.buscarComFiltros(
                    id, funcionario, local, sensor, dataEntrada,
                    horarioEntrada, dataSaida, horarioSaida, empresaId);

            return ResponseEntity.ok(registros);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/excluir/{empresaId}")
    public ResponseEntity<Void> excluirRegistrosDaEmpresa(
            @PathVariable Integer empresaId) {

        try {
            registroService.excluirTodosRegistrosDaEmpresa(empresaId);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
