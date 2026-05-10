package view;

import com.fasterxml.jackson.core.JsonProcessingException;
import control.EmpresaControl;
import control.ProdutoControl;
import model.Empresa;
import model.Produto;
import model.Sessao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

public class FrmProdDesativados extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private RFBarraPesquisa barraPesquisa;

    private JFrame frmPromptExcluir;
    private JFrame frmPromptAtivar;

    private EmpresaControl empresaControl = new EmpresaControl();
    private ProdutoControl produtoControl = new ProdutoControl();

    private String strPesquisa;

    private Color darkred;
    private Color verydarkgray;

    private JPanel pnlCabecalho;
    private JScrollPane pnlCenter;
    private JPanel pnlConteudo;
    private JPanel pnlRodape;
    private JPanel pnlTitulo;

    private RFTable tblProdutos;
    private DefaultTableModel dados;

    private RFTextField txtExcluir;
    private RFTextField txtReativar;

    private RFButton btnExcluir;
    private RFButton btnReativar;
    private RFButton btnExcluirDefinitivo;
    private RFButton btnReativarDefinitivo;
    private RFButton btnRecarregar;

    private GridBagConstraints constraintsSecundarias;
    private GridBagConstraints gridBagConstraints;

    private Insets insetsCabecalho;
    private Insets insetsRodape;

    private RFLabel lblTitulo;

    private JPanel pnlNavBar;
    private JPanel pnlTopo;

    private Empresa empresa;
    private List<Produto> listaProdNaoAtivos;

    public FrmProdDesativados() throws JsonProcessingException {
        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 36);
        } catch (Exception e) {
            e.printStackTrace();
        }

        darkred = new Color(155, 27, 48);
        verydarkgray = new Color(32, 35, 42);

        // Inicializa empresa e lista de produtos
        empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
        listaProdNaoAtivos = produtoControl.buscarProdutosNaoAtivos(empresa.getId());
        lblTitulo = new RFLabel("PRODUTOS DESATIVADOS");
        lblTitulo.setFont(lexendExa.deriveFont(Font.BOLD, 20));

        pnlTitulo = new JPanel();
        pnlTitulo.setBackground(verydarkgray);
        pnlTitulo.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlTitulo.add(lblTitulo);

        pnlNavBar = new JPanel();
        pnlTopo = new JPanel();

        pnlCabecalho = new JPanel();
        pnlRodape = new JPanel();
        pnlRodape.setBackground(verydarkgray);
        pnlRodape.setLayout(new GridBagLayout());
        pnlRodape.setPreferredSize(new Dimension(1280, 120));
        pnlRodape.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        pnlCenter = new JScrollPane();
        pnlCenter.setBackground(verydarkgray);
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlConteudo = new JPanel();

        tblProdutos = new RFTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável
            }
        };

        dados = new DefaultTableModel();

        String[] filtrosProdutos = {
                "ID",
                "Nome",
                "Categoria",
                "Preço",
                "Estoque",
                "Descrição",
                "Data Fabricação",
                "Data Validade",
                "Fornecedor"
        };

        barraPesquisa = new RFBarraPesquisa();
        barraPesquisa.setFiltros(filtrosProdutos);

        txtExcluir = new RFTextField();
        txtReativar = new RFTextField();

        btnExcluir = new RFButton("Excluir produto");
        btnReativar = new RFButton("Reativar produto");
        btnExcluirDefinitivo = new RFButton("Excluir");
        btnReativarDefinitivo = new RFButton("Reativar");
        btnRecarregar = new RFButton("Recarregar tabela");

        insetsCabecalho = new Insets(75, 0, 75, 0);
        insetsRodape = new Insets(50, 20, 50, 20);

        constraintsSecundarias = new GridBagConstraints();
        gridBagConstraints = new GridBagConstraints();

        pnlTopo.setLayout(new BoxLayout(pnlTopo, BoxLayout.Y_AXIS));

        pnlNavBar.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        pnlNavBar.setBackground(verydarkgray);

        pnlTopo.setOpaque(false);
        pnlNavBar.setOpaque(false);
        pnlTitulo.setOpaque(false);
        pnlCabecalho.setOpaque(false);
        pnlRodape.setOpaque(false);
        pnlCenter.setOpaque(false);
        pnlCenter.getViewport().setOpaque(false);
        pnlConteudo.setOpaque(false);

        // Menu de Navegação
        RFNavBar navBar = new RFNavBar(lexendExa, verydarkgray);
        //add(navBar, BorderLayout.NORTH);

        // Comportamento dos botões da navbar
        navBar.getBotao("Produto").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popup = new JPopupMenu();
                popup.setBackground(verydarkgray);

                JMenuItem opAtivos = new JMenuItem("Produtos Ativos");
                opAtivos.setBackground(verydarkgray);
                opAtivos.setForeground(Color.WHITE);
                opAtivos.setOpaque(true);

                JMenuItem opCadastrar = new JMenuItem("Cadastrar Produto");
                opCadastrar.setBackground(verydarkgray);
                opCadastrar.setForeground(Color.WHITE);
                opCadastrar.setOpaque(true);

                opAtivos.addActionListener(evt -> {
                    try {
                        new FrmProdGerenciamento();
                        dispose();
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                });

                opCadastrar.addActionListener(evt -> {
                    try {
                        new FrmProdCadastro();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                popup.add(opAtivos);
                popup.add(opCadastrar);
                popup.show(navBar.getBotao("Produto"), 0, navBar.getBotao("Produto").getHeight());
            }
        });

        navBar.getBotao("Funcionário").addActionListener(e -> {
            try {
                new FrmFuncGerenciamento();
                dispose();
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        });

        navBar.getBotao("Local").addActionListener(e -> {
            try {
                new FrmLocalGerenciamento();
                dispose();
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        navBar.getBotao("Registros").addActionListener(e -> {
            try {
                new FrmLocalRegistros();
                dispose();
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        });

        navBar.setOpaque(false);
        navBar.getBotao("Local").setOpaque(false);
        navBar.getBotao("Registros").setOpaque(false);
        navBar.getBotao("Local").setOpaque(false);
        navBar.getBotao("Funcionário").setOpaque(false);
        navBar.getBotao("Produto").setOpaque(false);

        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setLayout(new GridBagLayout());
        pnlCabecalho.setPreferredSize(new Dimension(1280, 115));

        pnlCenter.getViewport().setBackground(verydarkgray);

        // Aplica o scrollbar personalizado
        JScrollBar verticalScrollBar = pnlCenter.getVerticalScrollBar();
        verticalScrollBar.setUI(new RFFinoScrollBarUI());
        verticalScrollBar.setPreferredSize(new Dimension(5, 0));

        JScrollBar horizontalScrollBar = pnlCenter.getHorizontalScrollBar();
        horizontalScrollBar.setUI(new RFFinoScrollBarUI());
        horizontalScrollBar.setPreferredSize(new Dimension(0, 5));

        pnlConteudo.setLayout(new GridBagLayout());

        setTitle("RFIND - Produtos Desativados");
        setSize(960, 640);
        setLocationRelativeTo(null); // Primeiro centraliza
        // Depois ajusta para a direita
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) * 3 / 4; // 3/4 da tela para direita
        int y = (screenSize.height - getHeight()) / 2;   // Mantém no centro vertical
        setLocation(x, y);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === GRADIENTE ===
        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());

        inicializaTabela();
        configurarBarraPesquisa();

        pnlNavBar.add(navBar);

        pnlTopo.add(pnlNavBar);
        pnlTopo.add(pnlTitulo);
        pnlTopo.add(pnlCabecalho);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        pnlCabecalho.add(barraPesquisa, gridBagConstraints);

        constraintsSecundarias.insets = new Insets(15, 20, 15, 20);
        constraintsSecundarias.gridx = 0;
        constraintsSecundarias.gridy = 0;
        pnlRodape.add(btnRecarregar, constraintsSecundarias);

        constraintsSecundarias.gridx = 1;
        pnlRodape.add(btnReativar, constraintsSecundarias);

        constraintsSecundarias.gridx = 2;
        pnlRodape.add(btnExcluir, constraintsSecundarias);

        pnlConteudo.add(pnlCenter);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(verydarkgray);
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(pnlTopo, BorderLayout.NORTH);
        mainPanel.add(pnlCenter, BorderLayout.CENTER);
        mainPanel.add(pnlRodape, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        btnReativar.addActionListener(e -> {
            if (frmPromptAtivar == null)
                criaFrmPromptReativacao();
            frmPromptAtivar.setVisible(true);
        });

        btnExcluir.addActionListener(e -> {
            if (frmPromptExcluir == null) {
                try {
                    criaFrmPromptExclusao();
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            }
            frmPromptExcluir.setVisible(true);
        });

        barraPesquisa.addPesquisaListener((texto, filtro) -> {
            pesquisarDinamicamente();
        });

        btnRecarregar.addActionListener(e -> {
            try {
                limparPesquisa();
            } catch (JsonProcessingException ex) {
                RFMessageDialog.showMessageDialog(FrmProdDesativados.this,
                        "Erro ao recarregar tabela: " + ex.getMessage(),
                        "Erro");
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    private void inicializaTabela() throws JsonProcessingException {
        dados.setColumnCount(0);
        dados.addColumn("ID");
        dados.addColumn("Nome");
        dados.addColumn("Categoria");
        dados.addColumn("Preço");
        dados.addColumn("Estoque");
        dados.addColumn("Descrição");
        dados.addColumn("Data Fab.");
        dados.addColumn("Data validade");
        dados.addColumn("Fornecedor");
        dados.setNumRows(0);

        for (Produto p : listaProdNaoAtivos) {
            dados.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getCategoria(),
                    p.getPreco(),
                    p.getQuantidadeEstoque(),
                    p.getDescricao(),
                    formatarData(p.getDataFab()),
                    formatarData(p.getDataValidade()),
                    p.getFornecedor()
            });
        }
        tblProdutos.setModel(dados);
        tblProdutos.repaint();
        pnlCenter.setViewportView(tblProdutos);
    }

    private Sessao relembraSessao() {
        List<Sessao> sessao = Sessao.getSessao();
        return sessao.getFirst();
    }

    private void atualizaTabela() throws JsonProcessingException {
        empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
        listaProdNaoAtivos = produtoControl.buscarProdutosNaoAtivos(empresa.getId());

        dados.setNumRows(0);
        for (Produto produto : listaProdNaoAtivos) {
            dados.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    produto.getCategoria(),
                    produto.getPreco(),
                    produto.getQuantidadeEstoque(),
                    produto.getDescricao(),
                    formatarData(produto.getDataFab()),
                    formatarData(produto.getDataValidade()),
                    produto.getFornecedor()
            });
        }
        tblProdutos.setModel(dados);
    }

    private String formatarData(String dataOriginal) {
        if (dataOriginal == null || dataOriginal.isEmpty()) {
            return "";
        }
        String[] partes = dataOriginal.split("-");
        if (partes.length == 3) {
            return partes[2] + "/" + partes[1] + "/" + partes[0];
        }
        return dataOriginal;
    }

    private void pesquisarDinamicamente() {
        String texto = barraPesquisa.getTextoPesquisa();
        if (texto.isEmpty()) {
            try {
                atualizaTabela();
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
                RFMessageDialog.showMessageDialog(null, "Erro ao carregar dados", "Erro");
                return;
            }
        }

        String filtro = barraPesquisa.getFiltroSelecionado();
        Integer id = null;
        String nome = null, descricao = null, categoria = null, dataFab = null, dataValidade = null, fornecedor = null;
        Double preco = null;
        Integer quantidadeEstoque = null;

        try {
            int empresaId = relembraSessao().getId();

            switch (filtro) {
                case "ID":
                    try {
                        id = Integer.parseInt(texto);
                    } catch (NumberFormatException e) {
                        return;
                    }
                    break;
                case "Nome":
                    nome = texto;
                    break;
                case "Preço":
                    try {
                        preco = Double.parseDouble(texto);
                    } catch (NumberFormatException e) {
                        return;
                    }
                    break;
                case "Descrição":
                    descricao = texto;
                    break;
                case "Categoria":
                    categoria = texto;
                    break;
                case "Estoque":
                    try {
                        quantidadeEstoque = Integer.parseInt(texto);
                    } catch (NumberFormatException e) {
                        return;
                    }
                    break;
                case "Data Fabricação":
                    dataFab = texto;
                    break;
                case "Data Validade":
                    dataValidade = texto;
                    break;
                case "Fornecedor":
                    fornecedor = texto;
                    break;
            }

            List<Produto> resultados = produtoControl.buscarProdutosComFiltros(
                    id, nome, preco, descricao, categoria,
                    quantidadeEstoque, dataFab, dataValidade, fornecedor, empresaId);

            List<Produto> resultadosNaoAtivos = new ArrayList<>();
            for (Produto f : resultados) {
                if (!f.isAtivado()) {
                    resultadosNaoAtivos.add(f);
                }
            }

            dados.setRowCount(0);
            for (Produto p : resultadosNaoAtivos) {
                dados.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getCategoria(),
                        p.getPreco(),
                        p.getQuantidadeEstoque(),
                        p.getDescricao(),
                        formatarData(p.getDataFab()),
                        formatarData(p.getDataValidade()),
                        p.getFornecedor()
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            RFMessageDialog.showMessageDialog(null, "Erro ao pesquisar: " + ex.getMessage(), "Erro");
        }
    }

    private void configurarBarraPesquisa() {
        barraPesquisa.addPesquisaListener((texto, filtro) -> {
            pesquisarDinamicamente();
        });

        barraPesquisa.getBotaoPesquisar().addActionListener(e -> {
            pesquisarDinamicamente();
        });
    }

    private void limparPesquisa() throws JsonProcessingException {
        barraPesquisa.limparPesquisa();
        atualizaTabela();
    }

    private void criaFrmPromptReativacao() {
        frmPromptAtivar = new JFrame();
        frmPromptAtivar.setTitle("Digite o ID do produto");
        frmPromptAtivar.setSize(450, 250);
        frmPromptAtivar.getContentPane().setBackground(verydarkgray);
        frmPromptAtivar.setLayout(new GridBagLayout());
        frmPromptAtivar.setLocationRelativeTo(this);
        frmPromptAtivar.setResizable(false);
        frmPromptAtivar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(15, 0, 15, 0);
        frmPromptAtivar.add(txtReativar, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        frmPromptAtivar.add(btnReativarDefinitivo, gridBagConstraints);

        btnReativarDefinitivo.addActionListener(e -> {
            try {
                strPesquisa = txtReativar.getText();
                txtReativar.setText(null);
                produtoControl.ativaProduto(Integer.parseInt(strPesquisa), empresa.getId());
                RFMessageDialog.showMessageDialog(null, "Produto reativado com sucesso", "Aviso do sistema");
                frmPromptAtivar.dispose();
                listaProdNaoAtivos = produtoControl.buscarProdutosNaoAtivos(empresa.getId());
                atualizaTabela();
            } catch (Exception ex) {
                RFMessageDialog.showMessageDialog(null, "Produto não encontrado", "Erro");
            }
        });
    }

    private void criaFrmPromptExclusao() throws JsonProcessingException {
        frmPromptExcluir = new JFrame();
        frmPromptExcluir.setTitle("Digite o ID do produto");
        frmPromptExcluir.setSize(450, 250);
        frmPromptExcluir.getContentPane().setBackground(verydarkgray);
        frmPromptExcluir.setLayout(new GridBagLayout());
        frmPromptExcluir.setLocationRelativeTo(this);
        frmPromptExcluir.setResizable(false);
        frmPromptExcluir.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(15, 0, 15, 0);
        frmPromptExcluir.add(txtExcluir, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        frmPromptExcluir.add(btnExcluirDefinitivo, gridBagConstraints);

        btnExcluirDefinitivo.addActionListener(e -> {
            try {
                strPesquisa = txtExcluir.getText();
                txtExcluir.setText(null);
                produtoControl.deletByIdEmpresa(empresa, Integer.parseInt(strPesquisa));
                RFMessageDialog.showMessageDialog(null, "Produto excluído com sucesso", "Aviso do sistema");
                frmPromptExcluir.dispose();
                atualizaTabela();
            } catch (NullPointerException ex) {
                RFMessageDialog.showMessageDialog(null, "Digite o ID correto", "Erro");
            } catch (IllegalArgumentException ex) {
                RFMessageDialog.showMessageDialog(null, "O produto está ativado", "Erro");
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        });
    }
}