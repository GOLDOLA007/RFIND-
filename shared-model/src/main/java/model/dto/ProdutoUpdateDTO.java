package model.dto;

import model.Produto;

public class ProdutoUpdateDTO {
    private String nome;
    private Double preco;
    private String descricao;
    private String categoria;
    private Integer qntEstoque;
    private String  dataFab;
    private String dataValidade;
    private String fornecedor;

    public ProdutoUpdateDTO(String nome, Double preco, String descricao, String categoria, Integer qntEstoque, String dataFab, String dataValidade, String fornecedor) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.categoria = categoria;
        this.qntEstoque = qntEstoque;
        this.dataFab = dataFab;
        this.dataValidade = dataValidade;
        this.fornecedor = fornecedor;
    }

    public ProdutoUpdateDTO(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getQntEstoque() {
        return qntEstoque;
    }

    public void setQntEstoque(int qntEstoque) {
        this.qntEstoque = qntEstoque;
    }

    public String getDataFab() {
        return dataFab;
    }

    public void setDataFab(String dataFab) {
        this.dataFab = dataFab;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
}
