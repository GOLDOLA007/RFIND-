package model;
import jakarta.persistence.*;
import validator.LocalValidator;

@Entity
@Table
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String nome;
    @Column
    private String descricao;
    @Column(unique = true, nullable = false)
    private String idrfid;
    @ManyToOne()//cascade = CascadeType.ALL
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    public Local(String nome, String descricao, String idrfid, Empresa empresa) {
        this.nome = nome;
        this.descricao = descricao;
        this.idrfid = idrfid;
        this.empresa = empresa;
        LocalValidator.validate(this);
    }

    public Local(){}

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdrfid() {
        return idrfid;
    }

    public void setIdrfid(String idrfid) {
        this.idrfid = idrfid;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
