package control;

import ApiClient.RegistroClient;
import model.Produto;
import model.Registro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistroControl {

    RegistroClient registroClient;

    public RegistroControl() {
        registroClient = new RegistroClient();
    }

    public List<Registro> buscarRegistros(int empresaId){
        try {
            List<Registro> registros = registroClient.buscarPorEmpresa(empresaId);
            return registros;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Registro> buscarRegistrosComFiltros(
            Integer id,
            String funcionario,
            String local,
            String sensor,
            String dataEntrada,
            String horarioEntrada,
            String dataSaida,
            String horarioSaida,
            Integer empresaId) {

        try {
            return registroClient.buscarRegistrosComFiltros(
                    id, funcionario, local, sensor,
                    dataEntrada, horarioEntrada,
                    dataSaida, horarioSaida,
                    empresaId);
        } catch (IOException e) {
            System.err.println("Erro ao buscar registros com filtros: " + e.getMessage());
            return List.of(); // Retorna lista vazia em caso de erro
        }
    }

    public void excluirRegistrosPorEmpresa(int empresaId) throws Exception {
        try {
            registroClient.excluirRegistrosDaEmpresa(empresaId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Falha na comunicação com o servidor");
        }
    }

}
