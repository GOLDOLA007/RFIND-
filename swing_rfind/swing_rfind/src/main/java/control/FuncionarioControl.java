package control;

import ApiClient.FuncionarioClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import model.Empresa;
import model.DAO.FuncionarioDAO;
import model.Funcionario;
import model.dto.FuncionarioUpdateDTO;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class FuncionarioControl {
    private FuncionarioDAO funcionarioDAO;
    private FuncionarioClient funcionarioClient;
    public FuncionarioControl()
    {
        funcionarioDAO = new FuncionarioDAO();
        funcionarioClient = new FuncionarioClient();
    }
    public void insert(Funcionario funcionario){
        try {
            funcionarioClient.cadastrarFuncionario(funcionario);
        } catch (IOException e) {
            // tratar erro (mostrar JOptionPane, logar, etc)
            e.printStackTrace();
        }
    }
    public List<Funcionario> buscarAtivosPorEmpresa (int empresaId){
        try {
            return funcionarioClient.buscarAtivosPorEmpresa(empresaId);
        } catch (IOException e) {
            // tratar erro (mostrar JOptionPane, logar, etc)
            return null;
            // e.printStackTrace();
        }
    }

    public List<Funcionario> buscarNaoAtivosPorEmpresa (int empresaId){
        try {
            return funcionarioClient.buscarNaoAtivosPorEmpresa(empresaId);
        } catch (IOException e) {
            // tratar erro (mostrar JOptionPane, logar, etc)
            return null;
            // e.printStackTrace();
        }
    }

    public void atualizarFuncionario(int id, FuncionarioUpdateDTO dto) {
        try {
            funcionarioClient.atualizarFuncionario(id, dto);
            JOptionPane.showMessageDialog(null, "Funcionário atualizado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar funcionário.");
        }
    }

    public List<Funcionario> buscarComFiltros(Integer id, String nome, String sobrenome, String setor, String tag, int empresaId){
        try {
            return funcionarioClient.buscarComFiltros(id, nome, sobrenome, setor, tag, empresaId);
            //JOptionPane.showMessageDialog(null, "Pesquisa feita com sucesso!");
        } catch (IOException e) {
            //return null;
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Erro ao realizar pesquisa.");
        }
        return null;
    }

    public void desativarFuncionario(int id, int empresaId){
        funcionarioClient.desativarFuncionario(id, empresaId);
        //JOptionPane.showMessageDialog(null, "Funcionário atualizado com sucesso!");
    }

    public void ativarFuncionario(int id, int empresaId){
        funcionarioClient.ativarFuncionario(id, empresaId);
        //JOptionPane.showMessageDialog(null, "Funcionário atualizado com sucesso!");
    }

    public void excluirFuncionario(int id, int empresaId) throws JsonProcessingException {
        funcionarioClient.excluirFuncionario(id, empresaId);
    }

}