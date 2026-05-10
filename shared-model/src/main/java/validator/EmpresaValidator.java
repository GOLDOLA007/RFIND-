package validator;
import model.Empresa;
import validator.validationException.ValidationException;

public class EmpresaValidator {

    public static void validarSenha(String senha){
        if(senha == null || senha.length() < 8){
            throw new ValidationException("Senha deve ter no mínimo 8 caracteres");
        }
    }

    public static void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidationException("Nome é obrigatório");
        }

        if (nome.length() > 30) {
            throw new ValidationException("Nome deve ter no máximo 30 caracteres");
        }
    }

    public static void validarCnpj (String cnpj){

        if(cnpj == null || cnpj.trim().isEmpty()){
            throw new ValidationException("CNPJ é obrigatório");
        }

        // Remove caracteres não numéricos
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");

        // Verifica tamanho
        if (cnpjLimpo.length() != 14) {
            throw new ValidationException("CNPJ deve ter 14 dígitos");
        }

        // Verifica dígitos repetidos (ex: 00.000.000/0000-00)
        if (cnpjLimpo.matches("(\\d)\\1{13}")) {
            throw new ValidationException("CNPJ inválido (dígitos repetidos)");
        }

        // Calcula e compara os dígitos verificadores
        if (!validarDigitosVerificadores(cnpjLimpo)) {
            throw new ValidationException("CNPJ inválido (dígitos verificadores incorretos)");
        }

    }

    private static boolean validarDigitosVerificadores(String cnpj) {
        // Pega os 12 primeiros dígitos + primeiro dígito verificador
        String numeros = cnpj.substring(0, 12);
        String digitos = cnpj.substring(12);

        // Calcula o primeiro dígito verificador
        int digito1 = calcularDigito(numeros, 5);
        if (digito1 != Character.getNumericValue(digitos.charAt(0))) {
            return false;
        }

        // Calcula o segundo dígito verificador
        int digito2 = calcularDigito(numeros + digitos.charAt(0), 6);
        return digito2 == Character.getNumericValue(digitos.charAt(1));
    }

    private static int calcularDigito(String str, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;

        // Calcula a soma ponderada
        for (int i = 0; i < str.length(); i++) {
            soma += Character.getNumericValue(str.charAt(i)) * peso;
            peso = (peso == 2) ? 9 : peso - 1;
        }

        // Calcula o dígito
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    public static void validate(Empresa empresa){
        if(empresa == null){
            throw new ValidationException("Empresa não pode ser nula");
        }
        validarCnpj(empresa.getCnpj());
        validarNome(empresa.getNome());
        validarSenha(empresa.getSenha());
    }


}
