package control;

import ApiClient.SensorRfidClient;
import model.Empresa;
import model.Local;
import model.SensorRfid;

import java.io.IOException;
import java.util.List;

public class SensorRfidControl {

    private SensorRfidClient sensorRfidClient = new SensorRfidClient();

    public List<SensorRfid> getLocalDaEmpresa(Empresa empresa) throws IOException {
        return sensorRfidClient.buscarPorEmpresa(empresa.getId());
    }

    public List<SensorRfid> getNaoVinculados(Empresa empresa) throws IOException {
        return sensorRfidClient.buscarNaoVinculados(empresa.getId());
    }
}
