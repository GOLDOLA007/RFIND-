package com.rfindapi.api_rfind.controller;

//import com.rfindapi.api_rfind.JWT.JwtTokenService;
import model.Local;
import model.SensorRfid;
import model.RfidRequest;
import com.rfindapi.api_rfind.repository.SensorRfidRepository;
import com.rfindapi.api_rfind.Service.SensorRfidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiRfind/sensor")
public class SensorRfidController {

    @Autowired
    private SensorRfidRepository sensorRfidRepository;

    @Autowired
    private SensorRfidService sensorRfidService;

    @PostMapping("/cadastrar")
    public void cadastrar(@RequestBody SensorRfid sensorRfid) {
        sensorRfidService.insert(sensorRfid);
    }

    @DeleteMapping("/excluir/{id}")
    public void deleteById(@PathVariable int id) {
        sensorRfidRepository.deleteById(id);
    }

    @PostMapping("/verificar")
    public String verificarTag(@RequestBody RfidRequest request) {
        return sensorRfidService.verificarTag(request.getUid());
    }
    @GetMapping(value = "/buscar/empresa/{empresaId}")
    public List<SensorRfid> buscarPorEmpresa(@PathVariable("empresaId") int empresaId){
        return sensorRfidService.buscarporEmpresa(empresaId);
    }
    @GetMapping("/nao-vinculados/{empresaId}")
    public ResponseEntity<?> buscarSensoresNaoVinculados(@PathVariable("empresaId") int empresaId) {
        try {
            List<SensorRfid> sensores = sensorRfidService.buscarSensoresNaoVinculados(empresaId);
            return ResponseEntity.ok(sensores);
        } catch (Exception e) {
            e.printStackTrace(); // Log completo do erro no console
            return ResponseEntity.status(500)
                    .body("Erro ao buscar sensores não vinculados: " + e.getMessage());
        }
    }

}