package view;

import com.fasterxml.jackson.core.JsonProcessingException;
import control.EmpresaControl;
import control.FuncionarioControl;
import model.Empresa;
import model.Funcionario;
import model.Sessao;
import model.dto.FuncionarioUpdateDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FrmFuncDesativados extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private RFBarraPesquisa barraPesquisa;

    private JFrame frmPromptExcluir;
    private JFrame frmPromptAtivar;

    private EmpresaControl empresaControl = new EmpresaControl();
    private FuncionarioControl funcionarioControl = new FuncionarioControl();

    private String strPesquisa;

    private Color darkred;
    private Color verydarkgray;

    private JPanel pnlCabecalho;
    private JScrollPane pnlCenter;
    private JPanel pnlConteudo;
    private JPanel pnlRodape;
    private JPanel pnlTitulo;

    private RFTable tblFuncDes;
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

    private Empresa empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
    private List<Funcionario> listaFuncNaoAtivos = funcionarioControl.buscarNaoAtivosPorEmpresa(empresa.getId());

    public FrmFuncDesativados() throws JsonProcessingException {
        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 36);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicialização de cores e componentes
        darkred = new Color(155, 27, 48);
        verydarkgray = new Color(32, 35, 42);

        lblTitulo = new RFLabel("FUNCIONÁRIOS DESATIVADOS");
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

        pnlTitulo.setOpaque(false);
        pnlNavBar.setOpaque(false);
        pnlCabecalho.setOpaque(false);
        pnlRodape.setOpaque(false);
        pnlCenter.setOpaque(false);
        pnlCenter.getViewport().setOpaque(false);
        pnlConteudo.setOpaque(false);
        pnlTopo.setOpaque(false);

        tblFuncDes = new RFTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas as células não editáveis
            }
        };

        // Configura a tabela para ser transparente
        tblFuncDes.setOpaque(false);
        tblFuncDes.setBackground(new Color(0, 0, 0, 100)); // Fundo semi-transparente
        tblFuncDes.setForeground(Color.WHITE); // Texto branco para contraste

        dados = new DefaultTableModel();

        barraPesquisa = new RFBarraPesquisa();

        txtExcluir = new RFTextField();
        txtReativar = new RFTextField();

        btnExcluir = new RFButton("Excluir funcionário");
        btnReativar = new RFButton("Reativar funcionário");
        btnExcluirDefinitivo = new RFButton("Excluir");
        btnReativarDefinitivo = new RFButton("Reativar");
        btnRecarregar = new RFButton("Recarregar tabela");

        insetsCabecalho = new Insets(75, 0, 75, 0);
        insetsRodape = new Insets(50, 20, 50, 20);

        constraintsSecundarias = new GridBagConstraints();
        gridBagConstraints = new GridBagConstraints();

        // Configuração dos painéis
        pnlTopo.setLayout(new BorderLayout());
        pnlTopo.add(pnlTitulo, BorderLayout.NORTH);
        pnlTopo.add(pnlCabecalho, BorderLayout.CENTER);

        pnlNavBar.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        //pnlNavBar.setBackground(verydarkgray);

        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setLayout(new GridBagLayout());
        pnlCabecalho.setPreferredSize(new Dimension(1280, 115));

        // Configuração do scroll pane
        JScrollBar verticalScrollBar = pnlCenter.getVerticalScrollBar();
        verticalScrollBar.setUI(new RFFinoScrollBarUI());
        verticalScrollBar.setPreferredSize(new Dimension(5, 0));

        JScrollBar horizontalScrollBar = pnlCenter.getHorizontalScrollBar();
        horizontalScrollBar.setUI(new RFFinoScrollBarUI());
        horizontalScrollBar.setPreferredSize(new Dimension(0, 5));

        pnlConteudo.setLayout(new GridBagLayout());
        pnlConteudo.setBackground(verydarkgray);
        pnlConteudo.setBackground(verydarkgray);

        // Configuração do frame
        setTitle("RFIND - Funcionários Desativados");
        setSize(960, 640);

        setLocationRelativeTo(null); // Primeiro centraliza
        // Depois ajusta para a direita
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) * 3 / 4; // 3/4 da tela para direita
        int y = (screenSize.height - getHeight()) / 2;   // Mantém no centro vertical
        setLocation(x, y);

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializaTabela();
        configurarBarraPesquisa();

        // Adição da barra de pesquisa
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        pnlCabecalho.add(barraPesquisa, gridBagConstraints);

        // Adição de botões no rodapé
        constraintsSecundarias.insets = new Insets(15, 20, 15, 20);
        constraintsSecundarias.gridx = 1;
        constraintsSecundarias.gridy = 0;
        pnlRodape.add(btnRecarregar, constraintsSecundarias);

        constraintsSecundarias.gridx = 0;
        pnlRodape.add(btnReativar, constraintsSecundarias);

        constraintsSecundarias.gridx = 2;
        //pnlRodape.add(btnExcluir, constraintsSecundarias);

        // Configuração do layout principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(pnlTopo, BorderLayout.NORTH);
        mainPanel.add(pnlCenter, BorderLayout.CENTER);
        mainPanel.add(pnlRodape, BorderLayout.SOUTH);

        // Configuração da barra de navegação
        RFNavBar navBar = new RFNavBar(lexendExa, verydarkgray);
        //add(navBar, BorderLayout.NORTH);


        // Menu de navegação - Funcionário
        navBar.getBotao("Funcionário").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popup = new JPopupMenu();
                popup.setBackground(verydarkgray);

                JMenuItem opAtivos = new JMenuItem("Funcionários Ativos");
                opAtivos.setBackground(verydarkgray);
                opAtivos.setForeground(Color.WHITE);
                opAtivos.setOpaque(true);

                JMenuItem opCadastrar = new JMenuItem("Cadastrar Funcionário");
                opCadastrar.setBackground(verydarkgray);
                opCadastrar.setForeground(Color.WHITE);
                opCadastrar.setOpaque(true);

                opAtivos.addActionListener(evt -> {
                    try {
                        new FrmFuncGerenciamento();
                        dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                opCadastrar.addActionListener(evt -> {
                    try {
                        new FrmFuncCadastro();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                popup.add(opAtivos);
                popup.add(opCadastrar);
                popup.show(navBar.getBotao("Funcionário"), 0, navBar.getBotao("Funcionário").getHeight());
            }
        });

        // Menu de navegação - Produto
        navBar.getBotao("Produto").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FrmProdGerenciamento();
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        // Menu de navegação - Local
        navBar.getBotao("Local").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FrmLocalGerenciamento();
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        // Menu de navegação - Registros
        navBar.getBotao("Registros").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FrmLocalRegistros();
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        navBar.setOpaque(false);
        navBar.getBotao("Local").setOpaque(false);
        navBar.getBotao("Registros").setOpaque(false);
        navBar.getBotao("Local").setOpaque(false);
        navBar.getBotao("Funcionário").setOpaque(false);
        navBar.getBotao("Produto").setOpaque(false);

        pnlNavBar.add(navBar);

        // Listeners dos botões
        btnRecarregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    limparPesquisa();
                } catch (JsonProcessingException ex) {
                    RFMessageDialog.showMessageDialog(null, "Erro ao recarregar dados", "Erro");
                }
            }
        });

        btnReativar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frmPromptAtivar == null) {
                    criaFrmPromptReativacao();
                }
                frmPromptAtivar.setVisible(true);
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frmPromptExcluir == null) {
                    try {
                        criaFrmPromptExclusao();
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                frmPromptExcluir.setVisible(true);
            }
        });

        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(new BorderLayout());
        setContentPane(gradientPanel);
        gradientPanel.add(navBar, BorderLayout.NORTH);
        gradientPanel.add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void inicializaTabela() throws JsonProcessingException {
        dados.setColumnCount(0);
        dados.addColumn("ID");
        dados.addColumn("Nome");
        dados.addColumn("Sobrenome");
        dados.addColumn("Setor");
        dados.addColumn("TAG/Cartão");
        dados.setNumRows(0);
        for (Funcionario funcionario : listaFuncNaoAtivos) {
            dados.addRow(new Object[]{funcionario.getId(), funcionario.getNome(), funcionario.getSobrenome(), funcionario.getSetor(), funcionario.getTagRfid()});
        }
        tblFuncDes.setModel(dados);
        tblFuncDes.setBackground(verydarkgray);
        tblFuncDes.setForeground(Color.WHITE);
        tblFuncDes.setGridColor(Color.DARK_GRAY);
        tblFuncDes.setFillsViewportHeight(true);
        pnlCenter.setViewportView(tblFuncDes);
    }

    private Sessao relembraSessao() {
        List<Sessao> sessao = Sessao.getSessao();
        return sessao.getFirst();
    }

    private void atualizaTabelaDesativados() {
        listaFuncNaoAtivos = funcionarioControl.buscarNaoAtivosPorEmpresa(empresa.getId());
        dados.setNumRows(0);
        for (Funcionario funcionario : listaFuncNaoAtivos) {
            dados.addRow(new Object[]{
                    funcionario.getId(),
                    funcionario.getNome(),
                    funcionario.getSobrenome(),
                    funcionario.getSetor(),
                    funcionario.getTagRfid()
            });
        }
        tblFuncDes.setModel(dados);
    }

    private void criaFrmPromptReativacao() {
        frmPromptAtivar = new JFrame();
        frmPromptAtivar.setTitle("Digite o ID do funcionário");
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

        btnReativarDefinitivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    strPesquisa = txtReativar.getText();
                    txtReativar.setText(null);
                    funcionarioControl.ativarFuncionario(Integer.parseInt(strPesquisa), empresa.getId());
                    RFMessageDialog.showMessageDialog(null, "Funcionário reativado com sucesso", "Aviso do sistema");
                    //frmPromptAtivar.dispose();
                    atualizaTabelaDesativados();
                } catch (Exception ex) {
                    RFMessageDialog.showMessageDialog(null, "Funcionário não encontrado", "Erro");
                }
            }
        });
    }

    private void criaFrmPromptExclusao() throws JsonProcessingException {
        frmPromptExcluir = new JFrame();
        frmPromptExcluir.setTitle("Digite o ID do funcionário");
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

        btnExcluirDefinitivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    strPesquisa = txtExcluir.getText();
                    txtExcluir.setText(null);
                    funcionarioControl.excluirFuncionario(Integer.parseInt(strPesquisa), empresa.getId());
                    RFMessageDialog.showMessageDialog(null, "Funcionário excluído com sucesso", "Aviso do sistema");
                    atualizaTabelaDesativados();
                    frmPromptExcluir.dispose();
                } catch (NullPointerException ex) {
                    RFMessageDialog.showMessageDialog(null, "Digite o ID correto", "Erro");
                } catch (IllegalArgumentException ex) {
                    RFMessageDialog.showMessageDialog(null, "O funcionário está ativado", "Erro");
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void pesquisarDinamicamente() {
        String texto = barraPesquisa.getTextoPesquisa();
        if (texto.isEmpty()) {
            try {
                atualizaTabelaDesativados();
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
                RFMessageDialog.showMessageDialog(null, "Erro ao carregar dados", "Erro");
                return;
            }
        }

        String filtro = barraPesquisa.getFiltroSelecionado();
        Integer id = null;
        String nome = null, sobrenome = null, setor = null, tag = null;

        switch (filtro) {
            case "ID":
                try {
                    id = Integer.parseInt(texto);
                } catch (NumberFormatException e) {
                    return;
                }
                break;
            case "Nome/Sobrenome":
                nome = texto;
                sobrenome = texto;
                break;
            case "Setor":
                setor = texto;
                break;
            case "TAG/Cartão":
                tag = texto;
                break;
        }

        try {
            int empresaId = relembraSessao().getId();
            List<Funcionario> resultados = funcionarioControl.buscarComFiltros(id, nome, sobrenome, setor, tag, empresaId);

            List<Funcionario> resultadosNaoAtivos = new ArrayList<>();
            for (Funcionario f : resultados) {
                if (!f.getAtivado()) {
                    resultadosNaoAtivos.add(f);
                }
            }

            dados.setRowCount(0);
            for (Funcionario f : resultadosNaoAtivos) {
                dados.addRow(new Object[]{
                        f.getId(), f.getNome(), f.getSobrenome(), f.getSetor(), f.getTagRfid()
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
        atualizaTabelaDesativados();
    }
}