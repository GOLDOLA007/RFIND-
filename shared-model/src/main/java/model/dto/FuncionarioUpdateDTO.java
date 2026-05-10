package model.dto;

public class FuncionarioUpdateDTO {
    private String nome;
    private String sobrenome;
    private String setor;
    // private String cpf; // se quiser incluir depois
    private String tag; // opcional

    public FuncionarioUpdateDTO(String nome, String sobrenome, String setor, String tag) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.setor = setor;
        this.tag = tag;
    }

    public FuncionarioUpdateDTO(){

    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

}
