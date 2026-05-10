package control;

import ApiClient.LocalClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import model.DAO.LocalDAO;
import model.Empresa;
import model.Local;
import model.dto.LocalUpdateDTO;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class LocalControl {


    private LocalDAO lDao = new LocalDAO();
    private LocalClient localClient = new LocalClient();

    public void cadastrarLocal(Local local) throws JsonProcessingException {
        localClient.cadastrarLocal(local);
    }

    public List<Local> getLocalDaEmpresa(Empresa empresa) throws IOException {
        return localClient.buscarPorEmpresa(empresa.getId());
    }

    public List<Local> buscarLocaisComFiltros(Integer id,
                                              String nome,
                                              String descricao,
                                              String sensor,
                                              Empresa empresa) throws IOException {
        try {
            return localClient.buscarLocaisComFiltros(id, nome, descricao, sensor, empresa.getId());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao buscar locais: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }


    public List<Local> bucarLocais(String texto){
        //return lDao.BuscarLocais(texto);
        return null;
    }

    public void updateLocal(int id, LocalUpdateDTO dto) throws IOException {
        try {
            localClient.atualizarLocal(id, dto);
            JOptionPane.showMessageDialog(null, "Local atualizado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar Local.");
        }
    }

    public void excluirLocal(int id, int empresaId) throws JsonProcessingException {
        localClient.excluirLocal(id, empresaId);
    }

    public String getSensorLocal(String id){
       // return lDao.getCodSensorLocal(id);
        return null;
    }

}
