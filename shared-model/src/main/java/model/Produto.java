package model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Date;
@Entity
@Table
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private double preco;
    @Column
    private String descricao;
    @Column(nullable = false)
    private String categoria;
    @Column(nullable = false)
    private int quantidadeEstoque;
    @Column(nullable = false)
    private String dataFab;
    @Column(nullable = false)
    private String dataValidade;
    @Column(nullable = false)
    private String fornecedor;
    /*@Enumerated(EnumType.STRING)
    @Column
    private TipoEstadoProduto estadoProduto;*/
    @Column(nullable = false)
    private boolean ativado;
    @ManyToOne
    @JoinColumn(name = "empresa_id",nullable = false)
    //@JsonIgnore
    private Empresa empresa;

    public Produto(String nome,
                   double preco,
                   String descricao,
                   String categoria,
                   int quantidadeEstoque,
                   String dataFab,
                   String dataValidade,
                   String fornecedor,
                   boolean ativado, Empresa empresa) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.categoria = categoria;
        this.quantidadeEstoque = quantidadeEstoque;
        this.dataFab = dataFab;
        this.dataValidade = dataValidade;
        this.fornecedor = fornecedor;
        this.ativado = ativado;
        this.empresa = empresa;
    }

    public Produto(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
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

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
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

    public boolean isAtivado() {
        return ativado;
    }

    public void setAtivado(boolean ativado) {
        this.ativado = ativado;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
