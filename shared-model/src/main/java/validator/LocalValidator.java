package validator;

import model.Empresa;
import model.Local;
import validator.validationException.ValidationException;

public class LocalValidator {

    // Validação para nome
    public static void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidationException("Nome do local é obrigatório");
        }
        if (nome.length() > 100) {
            throw new ValidationException("Nome do local não pode exceder 100 caracteres");
        }
    }

    // Validação para descrição (opcional)
    public static void validarDescricao(String descricao) {
        if (descricao != null && descricao.length() > 500) {
            throw new ValidationException("Descrição não pode exceder 500 caracteres");
        }
    }

    // Validação para ID RFID
    public static void validarIdRfid(String idrfid) {
        if (idrfid == null || idrfid.trim().isEmpty()) {
            throw new ValidationException("ID RFID é obrigatório");
        }
        if (idrfid.length() != 10) {
            throw new ValidationException("ID RFID deve ter exatamente 10 caracteres");
        }
        if (!idrfid.matches("[a-fA-F0-9]+")) {
            throw new ValidationException("ID RFID deve conter apenas caracteres hexadecimais");
        }
    }

    // Validação para empresa
    public static void validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new ValidationException("Empresa é obrigatória");
        }
    }

    // Método para validar todo o Local de uma vez
    public static void validate(Local local) {
        if (local == null) {
            throw new ValidationException("Local não pode ser nulo");
        }

        validarNome(local.getNome());
        validarDescricao(local.getDescricao());
        //validarIdRfid(local.getIdrfid());
        validarEmpresa(local.getEmpresa());
    }
}