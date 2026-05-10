package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import control.EmpresaControl;
import control.ProdutoControl;
import model.*;
import model.dto.ProdutoUpdateDTO;

public class FrmProdGerenciamento extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private RFBarraPesquisa barraPesquisa;
    private JFrame frmPromptDesativar;

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

    private RFTextField txtDesativar;

    private RFButton btnExcluir;
    private RFButton btnDesativar;
    private RFButton btnRecarregar;

    private GridBagConstraints constraintsSecundarias;
    private GridBagConstraints gridBagConstraints;

    private Insets insetsCabecalho;
    private Insets insetsRodape;

    private RFLabel lblTitulo;

    private JPanel pnlNavBar;
    private JPanel pnlTopo;

    private Empresa empresa;
    private List<Produto> listaProdAtivos;

    public FrmProdGerenciamento() throws JsonProcessingException {
        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 36);
        } catch (Exception e) {
            e.printStackTrace();
        }

        darkred = new Color(155, 27, 48);
        verydarkgray = new Color(32, 35, 42);

        lblTitulo = new RFLabel("PRODUTOS ATIVOS");
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
                return column != 0; // Permite editar todas menos a coluna ID
            }
        };

        dados = new DefaultTableModel();

        barraPesquisa = new RFBarraPesquisa();

        txtDesativar = new RFTextField();

        btnExcluir = new RFButton("Desativar produto");
        btnDesativar = new RFButton("Desativar");
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

                JMenuItem opDesativados = new JMenuItem("Produtos Desativados");
                opDesativados.setBackground(verydarkgray);
                opDesativados.setForeground(Color.WHITE);
                opDesativados.setOpaque(true);

                JMenuItem opCadastrar = new JMenuItem("Cadastrar Produto");
                opCadastrar.setBackground(verydarkgray);
                opCadastrar.setForeground(Color.WHITE);
                opCadastrar.setOpaque(true);

                opDesativados.addActionListener(evt -> {
                    try {
                        new FrmProdDesativados();
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

                popup.add(opDesativados);
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

        setTitle("RFIND - Gerenciamento de Produtos");
        setSize(960, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === GRADIENTE ===
        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());


        // Carrega dados iniciais
        empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
        listaProdAtivos = produtoControl.buscarProdutosAtivos(empresa.getId());

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
        pnlRodape.add(btnExcluir, constraintsSecundarias);

        constraintsSecundarias.gridx = 1;
        pnlRodape.add(btnRecarregar, constraintsSecundarias);

        pnlConteudo.add(pnlCenter);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(verydarkgray);
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(pnlTopo, BorderLayout.NORTH);
        mainPanel.add(pnlCenter, BorderLayout.CENTER);
        mainPanel.add(pnlRodape, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        barraPesquisa.addPesquisaListener((texto, filtro) -> {
            pesquisarDinamicamente();
        });

        btnRecarregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparPesquisa();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frmPromptDesativar == null) {
                    criaFrmPromptDesativacao();
                }
                frmPromptDesativar.setVisible(true);
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
        dados.addColumn("Data Validade");
        dados.addColumn("Fornecedor");

        dados.setNumRows(0);
        for (Produto produto : listaProdAtivos) {
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
        tblProdutos.repaint();
        pnlCenter.setViewportView(tblProdutos);

        dados.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == 0) {
                    return;
                }

                if (!validarEdicaoProduto(row)) {
                    try {
                        atualizaTabelaAtivos();
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                    return;
                }

                int id = (int) dados.getValueAt(row, 0);
                String nome = (String) dados.getValueAt(row, 1);
                String categoria = (String) dados.getValueAt(row, 2);
                Double preco = (Double) dados.getValueAt(row, 3);
                Integer qtnEstoque = (Integer) dados.getValueAt(row, 4);
                String descricao = (String) dados.getValueAt(row, 5);
                String dataFab = (String) dados.getValueAt(row, 6);
                String dataValidade = (String) dados.getValueAt(row, 7);
                String fornecedor = (String) dados.getValueAt(row, 8);

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Deseja realmente atualizar o produto ID " + id + "?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    ProdutoUpdateDTO dto = new ProdutoUpdateDTO();
                    dto.setNome(nome);
                    dto.setCategoria(categoria);
                    dto.setPreco(preco);
                    dto.setQntEstoque(qtnEstoque);
                    dto.setDescricao(descricao);
                    dto.setDataFab(formatarData(dataFab));
                    dto.setDataValidade(formatarData(dataValidade));
                    dto.setFornecedor(fornecedor);

                    try {
                        produtoControl.atualizarProduto(id, dto);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Erro ao atualizar produto: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        atualizaTabelaAtivos();
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void atualizaTabelaAtivos() throws JsonProcessingException {
        empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
        listaProdAtivos = produtoControl.buscarProdutosAtivos(empresa.getId());

        dados.setNumRows(0);
        for (Produto produto : listaProdAtivos) {
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

    private Sessao relembraSessao() {
        List<Sessao> sessao = Sessao.getSessao();
        return sessao.getFirst();
    }

    private void criaFrmPromptDesativacao() {
        frmPromptDesativar = new JFrame();
        frmPromptDesativar.setTitle("Digite o ID do produto");
        frmPromptDesativar.setSize(450, 250);
        frmPromptDesativar.getContentPane().setBackground(verydarkgray);
        frmPromptDesativar.setLayout(new GridBagLayout());
        frmPromptDesativar.setLocationRelativeTo(this);
        frmPromptDesativar.setResizable(false);
        frmPromptDesativar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(15, 0, 15, 0);
        frmPromptDesativar.add(txtDesativar, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        frmPromptDesativar.add(btnDesativar, gridBagConstraints);

        btnDesativar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    strPesquisa = txtDesativar.getText();
                    txtDesativar.setText(null);
                    produtoControl.desativaProduto(Integer.parseInt(strPesquisa), empresa.getId());
                    RFMessageDialog.showMessageDialog(null, "Produto desativado", "Aviso do sistema");
                    frmPromptDesativar.dispose();
                    atualizaTabelaAtivos();
                } catch (Exception ex) {
                    RFMessageDialog.showMessageDialog(null, "Produto não encontrado", "Erro");
                }
            }
        });
    }

    private void pesquisarDinamicamente() {
        String texto = barraPesquisa.getTextoPesquisa();
        if (texto.isEmpty()) {
            try {
                atualizaTabelaAtivos();
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

            dados.setRowCount(0);
            for (Produto p : resultados) {
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
        String[] filtrosProdutos = {
                "ID",
                "Nome",
                "Preço",
                "Descrição",
                "Categoria",
                "Estoque",
                "Data Fabricação",
                "Data Validade",
                "Fornecedor"
        };

        barraPesquisa.setFiltros(filtrosProdutos);

        barraPesquisa.addPesquisaListener((texto, filtro) -> {
            pesquisarDinamicamente();
        });

        barraPesquisa.getBotaoPesquisar().addActionListener(e -> {
            pesquisarDinamicamente();
        });
    }

    private void limparPesquisa() {
        barraPesquisa.limparPesquisa();
        try {
            atualizaTabelaAtivos();
        } catch (JsonProcessingException ex) {
            RFMessageDialog.showMessageDialog(null, "Erro ao recarregar dados", "Erro");
        }
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

    private boolean validarEdicaoProduto(int row) {
        try {
            String nome = (String) dados.getValueAt(row, 1);
            String categoria = (String) dados.getValueAt(row, 2);
            Object precoObj = dados.getValueAt(row, 3);
            Object estoqueObj = dados.getValueAt(row, 4);
            String descricao = (String) dados.getValueAt(row, 5);
            String dataFab = (String) dados.getValueAt(row, 6);
            String dataValidade = (String) dados.getValueAt(row, 7);
            String fornecedor = (String) dados.getValueAt(row, 8);

            if (nome == null || nome.trim().isEmpty()) {
                RFMessageDialog.showMessageDialog(null, "Nome do produto não pode ser vazio!", "Erro de Validação");
                return false;
            }
            if (nome.length() > 100) {
                RFMessageDialog.showMessageDialog(null, "Nome do produto não pode ter mais de 100 caracteres!", "Erro de Validação");
                return false;
            }

            if (categoria == null || categoria.trim().isEmpty()) {
                RFMessageDialog.showMessageDialog(null, "Categoria não pode ser vazia!", "Erro de Validação");
                return false;
            }
            if (categoria.length() > 50) {
                RFMessageDialog.showMessageDialog(null, "Categoria não pode ter mais de 50 caracteres!", "Erro de Validação");
                return false;
            }

            double preco = 0;
            try {
                preco = Double.parseDouble(precoObj.toString());
                if (preco <= 0) {
                    RFMessageDialog.showMessageDialog(null, "Preço deve ser maior que zero!", "Erro de Validação");
                    return false;
                }
            } catch (Exception e) {
                RFMessageDialog.showMessageDialog(null, "Preço deve ser um número válido!", "Erro de Validação");
                return false;
            }

            int estoque = 0;
            try {
                estoque = Integer.parseInt(estoqueObj.toString());
                if (estoque < 0) {
                    RFMessageDialog.showMessageDialog(null, "Estoque não pode ser negativo!", "Erro de Validação");
                    return false;
                }
            } catch (Exception e) {
                RFMessageDialog.showMessageDialog(null, "Estoque deve ser um número inteiro!", "Erro de Validação");
                return false;
            }

            if (descricao != null && descricao.length() > 255) {
                RFMessageDialog.showMessageDialog(null, "Descrição não pode ter mais de 255 caracteres!", "Erro de Validação");
                return false;
            }

            if (dataFab != null && !dataFab.isEmpty()) {
                if (!validarFormatoData(dataFab)) {
                    RFMessageDialog.showMessageDialog(null, "Formato de data deve ser dd/MM/yyyy", "Erro de Validação");
                    return false;
                }
                if (!validarDataConteudo(dataFab)) {
                    RFMessageDialog.showMessageDialog(null, "Data de fabricação inválida", "Erro de Validação");
                    return false;
                }
            }

            if (dataValidade != null && !dataValidade.isEmpty()) {
                if (!validarFormatoData(dataValidade)) {
                    RFMessageDialog.showMessageDialog(null, "Formato de data deve ser dd/MM/yyyy", "Erro de Validação");
                    return false;
                }
                if (!validarDataConteudo(dataValidade)) {
                    RFMessageDialog.showMessageDialog(null, "Data de validade inválida", "Erro de Validação");
                    return false;
                }
            }

            if (fornecedor == null || fornecedor.trim().isEmpty()) {
                RFMessageDialog.showMessageDialog(null, "Fornecedor não pode ser vazio!", "Erro de Validação");
                return false;
            }
            if (fornecedor.length() > 100) {
                RFMessageDialog.showMessageDialog(null, "Fornecedor não pode ter mais de 100 caracteres!", "Erro de Validação");
                return false;
            }

            return true;

        } catch (Exception e) {
            RFMessageDialog.showMessageDialog(null, "Erro ao validar dados: " + e.getMessage(), "Erro");
            return false;
        }
    }

    private boolean validarFormatoData(String data) {
        return data.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    private boolean validarDataConteudo(String data) {
        try {
            String[] partes = data.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);

            int anoAtual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            if (ano > anoAtual) {
                return false;
            }

            if (mes < 1 || mes > 12) {
                return false;
            }

            if (dia < 1) {
                return false;
            }

            if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                if (dia > 31) {
                    return false;
                }
            } else if (mes == 2) {
                if (dia > 28) {
                    return false;
                }
            } else {
                if (dia > 30) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}