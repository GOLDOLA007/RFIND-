package Util;

import control.FuncionarioControl;
import model.Funcionario;
import java.util.List;

public class PesquisaUtil {

    public interface PesquisaCallback {
        void onResultado(List<Funcionario> resultados);
        void onErro(Exception ex);
    }

    public static void pesquisarFuncionarios(FuncionarioControl control,
                                             Integer empresaId,
                                             String filtro,
                                             String texto,
                                             PesquisaCallback callback) {

        Integer id = null;
        String nome = null, sobrenome = null, setor = null, tag = null;

        switch (filtro) {
            case "ID":
                try {
                    id = Integer.parseInt(texto);
                } catch (NumberFormatException e) {
                    callback.onErro(new IllegalArgumentException("ID inválido"));
                    return;
                }
                break;
            case "Nome/Sobrenome":
                nome = texto;
                sobrenome = texto;
                break;
            case "Setor":
                setor = texto;
                break;
            case "TAG/Cartão":
                tag = texto;
                break;
        }

        try {
            List<Funcionario> resultados = control.buscarComFiltros(id, nome, sobrenome, setor, tag, empresaId);
            callback.onResultado(resultados);
        } catch (Exception ex) {
            callback.onErro(ex);
        }
    }
}