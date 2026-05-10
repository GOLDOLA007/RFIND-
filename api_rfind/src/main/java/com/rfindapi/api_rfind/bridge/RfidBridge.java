package com.rfindapi.api_rfind.bridge;

import com.fazecast.jSerialComm.*;
import com.rfindapi.api_rfind.Service.EmpresaService;
import com.rfindapi.api_rfind.Service.SensorRfidService;
import com.rfindapi.api_rfind.repository.EmpresaRepository;
import com.rfindapi.api_rfind.repository.SensorRfidRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import model.Empresa;
import model.SensorRfid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RfidBridge {
    private static final Logger logger = LoggerFactory.getLogger(RfidBridge.class);
    private static final int MAX_UID_LENGTH = 20;
    private static final int MIN_UID_LENGTH = 4;

    @Autowired
    private SensorRfidRepository sensorRfidRepository;

    @Autowired
    private EmpresaService empresaService;

    @Value("${serial.port:COM5}")
    private String portName;

    private final SensorRfidService sensorRfidService;
    private SerialPort serialPort;
    @Autowired
    private EmpresaRepository empresaRepository;

    public RfidBridge(SensorRfidService sensorRfidService, SensorRfidRepository sensorRfidRepository) {
        this.sensorRfidService = sensorRfidService;
        this.sensorRfidRepository = sensorRfidRepository;
    }

    @PostConstruct
    public void init() {
        try {
            serialPort = SerialPort.getCommPort(portName);
            serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        try {
                            byte[] buffer = new byte[serialPort.bytesAvailable()];
                            int numRead = serialPort.readBytes(buffer, buffer.length);
                            String receivedData = new String(buffer, 0, numRead).trim();

                            if (isValidUid(receivedData)) {
                                logger.debug("UID válido recebido: {}", receivedData);
                                processValidUid(receivedData);
                            } else {
                                logger.debug("Mensagem ignorada: {}", receivedData);
                            }
                        } catch (Exception e) {
                            logger.error("Erro durante leitura serial", e);
                        }
                    }
                }
            });

            if (serialPort.openPort()) {
                logger.info("Porta serial {} aberta com sucesso", portName);
            } else {
                logger.error("Falha ao abrir porta serial {}", portName);
            }
        } catch (Exception e) {
            logger.error("Erro na inicialização da porta serial", e);
        }
    }

    private boolean isValidUid(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }

        // Ignora mensagens de status/erro
        if (data.startsWith("Status:") ||
                data.startsWith("Erro") ||
                data.contains("NEGADO") ||
                data.contains("ACESSO")) {
            return false;
        }

        // Verifica se tem o formato IDEMPRESA_SENSORID:UID
        if(data.contains(":")){
            String[] parts = data.split(":");
            return parts.length == 2 &&
                    parts[0].matches("^\\d+_SENSOR\\d{4}$") && // Formato: 1_SENSOR0001
                    parts[1].matches("^[a-fA-F0-9]{4,20}$");   // Formato UID
        }
        return false;
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (serialPort != null && serialPort.isOpen()) {
                serialPort.removeDataListener();
                serialPort.closePort();
                logger.info("Porta serial fechada");
            }
        } catch (Exception e) {
            logger.error("Erro ao fechar porta serial", e);
        }
    }

    private void processValidUid(String data) {
        try {
            String[] parts = data.split(":");
            if (parts.length != 2) {
                logger.error("Formato inválido. Esperado: IDEMPRESA_SENSORID:UID");
                return;
            }

            String sensorComEmpresa = parts[0];
            String uid = parts[1];

            // Separa empresaId e sensorId
            String[] ids = sensorComEmpresa.split("_");
            if (ids.length != 2) {
                logger.error("Formato inválido. Esperado: IDEMPRESA_SENSORID");
                return;
            }

            int empresaId;
            try {
                empresaId = Integer.parseInt(ids[0]);
            } catch (NumberFormatException e) {
                logger.error("ID da empresa inválido: {}", ids[0]);
                return;
            }

            String sensorId = ids[1];

            // Busca a empresa
            Optional<Empresa> empresaOpt = Optional.ofNullable(empresaRepository.findById(empresaId));
            if (!empresaOpt.isPresent()) {
                logger.error("Empresa não encontrada para ID: {}", empresaId);
                return;
            }
            Empresa empresa = empresaOpt.get();

            // Verifica/Cadastra sensor
            Optional<SensorRfid> sensorExistente = sensorRfidRepository
                    .findByNomeAndEmpresa(sensorId, empresa);

            if (!sensorExistente.isPresent()) {
                SensorRfid novoSensor = new SensorRfid(sensorId, empresa);
                sensorRfidRepository.save(novoSensor);
                logger.info("Novo sensor registrado: {} para empresa {}", sensorId, empresa.getNome());
            }

            // Processa a leitura do UID
            String resposta = sensorRfidService.processarUid(uid, empresaId);

            // Envia resposta para o Arduino
            if (serialPort.isOpen()) {
                serialPort.writeBytes((resposta + "\n").getBytes(), resposta.length() + 1);
                logger.debug("Resposta enviada: {}", resposta);
            }

        } catch (Exception e) {
            logger.error("Erro ao processar leitura", e);
            if (serialPort.isOpen()) {
                serialPort.writeBytes("ERRO\n".getBytes(), 5);
            }
        }
    }
}