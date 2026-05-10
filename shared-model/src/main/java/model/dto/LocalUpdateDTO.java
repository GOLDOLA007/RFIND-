package model.dto;

public class LocalUpdateDTO {

    private String nome;
    private String descricao;
    private String sensor;

    public LocalUpdateDTO(String nome, String descricao, String sensor) {
        this.nome = nome;
        this.descricao = descricao;
        this.sensor = sensor;
    }

    public LocalUpdateDTO() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }
}
