package validator;

import model.Empresa;
import model.Funcionario;
import validator.validationException.ValidationException;

public class FuncionarioValidator {


    // Validação para nome
    public static void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidationException("Nome é obrigatório");
        }
        if (nome.length() > 50) {
            throw new ValidationException("Nome não pode exceder 50 caracteres");
        }
    }

    // Validação para sobrenome
    public static void validarSobrenome(String sobrenome) {
        if (sobrenome == null || sobrenome.trim().isEmpty()) {
            throw new ValidationException("Sobrenome é obrigatório");
        }
        if (sobrenome.length() > 100) {
            throw new ValidationException("Sobrenome não pode exceder 100 caracteres");
        }
    }

    // Validação para setor
    public static void validarSetor(String setor) {
        if (setor == null || setor.trim().isEmpty()) {
            throw new ValidationException("Setor é obrigatório");
        }
        if (setor.length() > 50) {
            throw new ValidationException("Setor não pode exceder 50 caracteres");
        }
    }

    // Validação para tag RFID
    public static void validarTagRfid(String tagRfid) {
        if (tagRfid == null || tagRfid.trim().isEmpty()) {
            throw new ValidationException("Tag RFID é obrigatória");
        }
        if (tagRfid.length() != 8) {
            throw new ValidationException("Tag RFID deve ter exatamente 8 caracteres");
        }
        if (!tagRfid.matches("[a-fA-F0-9]+")) {
            throw new ValidationException("Tag RFID deve conter apenas caracteres hexadecimais");
        }
    }

    // Validação para empresa
    public static void validarEmpresa(Empresa empresa) {
        if (empresa == null) {
            throw new ValidationException("Empresa é obrigatória");
        }
        // Você pode adicionar mais validações específicas da empresa se necessário
    }

    public static void validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new ValidationException("CPF é obrigatório");
        }

        // Remove caracteres não numéricos
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        // Verifica tamanho
        if (cpfLimpo.length() != 11) {
            throw new ValidationException("CPF deve ter 11 dígitos");
        }

        // Verifica dígitos repetidos (ex: 000.000.000-00)
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            throw new ValidationException("CPF inválido (dígitos repetidos)");
        }

        // Calcula e compara os dígitos verificadores
        if (!validarDigitosVerificadoresCpf(cpfLimpo)) {
            throw new ValidationException("CPF inválido (dígitos verificadores incorretos)");
        }
    }

    private static boolean validarDigitosVerificadoresCpf(String cpf) {
        // Pega os 9 primeiros dígitos
        String numeros = cpf.substring(0, 9);
        String digitos = cpf.substring(9);

        // Calcula o primeiro dígito verificador
        int digito1 = calcularDigitoCpf(numeros, 10);
        if (digito1 != Character.getNumericValue(digitos.charAt(0))) {
            return false;
        }

        // Calcula o segundo dígito verificador
        int digito2 = calcularDigitoCpf(numeros + digitos.charAt(0), 11);
        return digito2 == Character.getNumericValue(digitos.charAt(1));
    }

    private static int calcularDigitoCpf(String str, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;

        // Calcula a soma ponderada
        for (int i = 0; i < str.length(); i++) {
            soma += Character.getNumericValue(str.charAt(i)) * peso;
            peso--;
        }

        // Calcula o dígito
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    // Método para validar todo o funcionário de uma vez
    public static void validate(Funcionario funcionario) {
        if (funcionario == null) {
            throw new ValidationException("Funcionário não pode ser nulo");
        }

        validarCpf(funcionario.getCpf());
        validarNome(funcionario.getNome());
        validarSobrenome(funcionario.getSobrenome());
        validarSetor(funcionario.getSetor());
        validarTagRfid(funcionario.getTagRfid());
        validarEmpresa(funcionario.getEmpresa());
    }
}
