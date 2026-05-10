package com.rfindapi.api_rfind.Service;

import com.rfindapi.api_rfind.repository.FuncionarioRepository;
import com.rfindapi.api_rfind.repository.LocalRepository;
import com.rfindapi.api_rfind.repository.RegistroRepository;
import model.*;
import com.rfindapi.api_rfind.repository.SensorRfidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SensorRfidService {

    private final RestTemplate restTemplate;
    private final SensorRfidRepository sensorRfidRepository;
    private static final Logger logger = LoggerFactory.getLogger(SensorRfidService.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository; // Alterado para usar o repositório correto

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private LocalRepository localRepository;

    public SensorRfid insert(SensorRfid sensorRfid) {
        if(sensorRfidRepository.findById(sensorRfid.getId()).isPresent()){
            throw new RuntimeException("SensorRfid Already Exists");
        }
        return sensorRfidRepository.save(sensorRfid);
    }

    public List<SensorRfid> buscarporEmpresa(int empresaId) {
        return sensorRfidRepository.findPorEmpresa(empresaId);
    }

    public List<SensorRfid> buscarSensoresNaoVinculados(int empresaId) {
        return sensorRfidRepository.findSensoresNaoVinculados(empresaId);
    }


    @Autowired
    public SensorRfidService(RestTemplate restTemplate,
                             SensorRfidRepository sensorRfidRepository) {
        this.restTemplate = restTemplate;
        this.sensorRfidRepository = sensorRfidRepository;
    }

    public String verificarTag(String uid) {
        // Log completo do UID recebido (hex dump)
        logger.info("UID Recebido (RAW): {}", Arrays.toString(uid.getBytes(StandardCharsets.UTF_8)));
        logger.info("UID Recebido (String): '{}'", uid);
        logger.info("Tamanho: {} caracteres", uid.length());

        // Debug avançado
        String trimmedUid = uid.trim();
        logger.info("UID Trimmed: '{}' (Tamanho: {})", trimmedUid, trimmedUid.length());

        // Consulta com log da query
        logger.debug("Executando query por tag: '{}'", trimmedUid);
        Optional<Funcionario> funcionario = funcionarioRepository.findByTagRfid(trimmedUid);

        if (funcionario.isPresent()) {
            Funcionario f = funcionario.get();
            logger.info("Funcionário encontrado - ID: {}, Nome: {}, Tag: '{}'",
                    f.getId(), f.getNome(), f.getTagRfid());
            return "LIBERADO";
        } else {
            logger.warn("NENHUM funcionário encontrado para a tag: '{}'", trimmedUid);

            // Log adicional para debug
            List<String> todasTags = funcionarioRepository.findAllTagsRfid();
            logger.info("Tags RFID cadastradas no sistema ({}): {}", todasTags.size(), todasTags);

            return "NEGADO";
        }
    }

    public String processarUid(String uid, int empresaId) {
        try {
            // 1. Verificar se a tag pertence a um funcionário
            Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByTagRfid(uid.trim());

            if (!funcionarioOpt.isPresent()) {
                return "NEGADO";
            }

            Funcionario funcionario = funcionarioOpt.get();

            // 2. Obter o sensor/local correspondente
            String sensorId = "SENSOR" + String.format("%04d", empresaId); // Formato SENSOR0001
            Optional<Local> localOpt = localRepository.findByIdrfid(sensorId);

            if (!localOpt.isPresent()) {
                logger.error("Local não encontrado para sensor: {}", sensorId);
                return "NEGADO";
            }

            Local local = localOpt.get();

            // 3. Verificar se já existe um registro aberto para este funcionário neste local
            Optional<Registro> registroAberto = registroRepository.findRegistroAberto(funcionario.getId(), local);

            DateTimeFormatter formatter12h = DateTimeFormatter.ofPattern("hh:mm:ss a");
            if (registroAberto.isPresent()) {
                // Registrar saída
                Registro registro = registroAberto.get();
                registro.setDataSaida(LocalDate.now().toString());
                registro.setHoraSaida(LocalTime.now().withNano(0).format(formatter12h).toString());
                registroRepository.save(registro);
                return "SAIDA_REGISTRADA";
            } else {
                // Registrar entrada
                Registro novoRegistro = new Registro();
                novoRegistro.setFuncionario(funcionario);
                novoRegistro.setLocal(local);
                novoRegistro.setDataEntrada(LocalDate.now().toString());
                novoRegistro.setHoraEntrada(LocalTime.now().withNano(0).format(formatter12h).toString());
                novoRegistro.setEmpresa(funcionario.getEmpresa());
                registroRepository.save(novoRegistro);
                return "ENTRADA_REGISTRADA";
            }

        } catch (Exception e) {
            logger.error("Erro ao processar UID: " + uid, e);
            return "ERRO";
        }
    }
}