package control;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import ApiClient.EmpresaClient;
import model.Empresa;

public class EmpresaControl {
    //private EmpresaDAO empresaDAO;
    private EmpresaClient empresaClient;

    public EmpresaControl()
    {
        //empresaDAO = new EmpresaDAO();
        empresaClient = new EmpresaClient();
    }
    public void insert(Empresa empresa) throws JsonProcessingException {
        try {
            empresaClient.cadastrarEmpresa(empresa);
        } catch (IOException e) {
            // tratar erro (mostrar JOptionPane, logar, etc)
            e.printStackTrace();
        }
    }

    public Empresa findCnpj(String cnpj) throws JsonProcessingException {
        try {
            return empresaClient.localizarCnpjEmpresa(cnpj);
        } catch (IOException e) {
            // tratar erro (mostrar JOptionPane, logar, etc)
            e.printStackTrace();
        }
        return null;
    }

    public Empresa findEmail(String email) throws JsonProcessingException {
        try {
            return empresaClient.localizarEmailEmpresa(email);
        } catch (IOException e) {
            // tratar erro (mostrar JOptionPane, logar, etc)
            e.printStackTrace();
        }
        return null;
    }
}