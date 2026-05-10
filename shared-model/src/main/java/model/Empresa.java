package model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import validator.EmpresaValidator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 320, unique = true)
    private String email;
    @Column(nullable = false, length = 14, unique = true)
    private String cnpj;
    @Column(unique = true, length = 30)
    private String nome;
    @Column(length = 30)
    private String senha;
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Funcionario> funcionarios = new ArrayList<Funcionario>();
    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Produto> produtos = new ArrayList<Produto>();

    public Empresa(String cnpj, String nome, String email, String senha)
    {
        this.cnpj = cnpj;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        EmpresaValidator.validate(this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Empresa(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}