package model;

import jakarta.persistence.*;
import validator.FuncionarioValidator;

@Entity
@Table
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 11, nullable = false, unique = true)
    private String cpf;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String sobrenome;
    @Column(nullable = false)
    private String setor;
    @Column(nullable = false)
    private boolean ativado;
    @Column(name="tag_rfid",unique = true, nullable = false, length = 32)
    private String tagRfid;
    @ManyToOne()//cascade = CascadeType.ALL
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    public Funcionario(String cpf, String nome, String sobrenome, String setor, boolean ativado, String tagRfid, Empresa empresa) {
        this.cpf = cpf;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.setor = setor;
        this.ativado = ativado;
        this.tagRfid = tagRfid;
        this.empresa = empresa;
        FuncionarioValidator.validate(this);
    }

    public Funcionario(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public boolean isAtivado() {
        return ativado;
    }

    public void setAtivado(boolean ativado) {
        this.ativado = ativado;
    }

    public String getTagRfid() {
        return tagRfid;
    }

    public void setTagRfid(String tagRfid) {
        this.tagRfid = tagRfid;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public boolean getAtivado() {
        return ativado;
    }
}
