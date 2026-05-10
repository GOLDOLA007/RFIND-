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
import control.LocalControl;
import model.Empresa;
import model.Local;
import model.Sessao;
import model.dto.LocalUpdateDTO;

public class FrmLocalGerenciamento extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private RFBarraPesquisa barraPesquisa;

    private JFrame frmPromptDesativar;

    private EmpresaControl empresaControl = new EmpresaControl();
    private LocalControl localControl = new LocalControl();

    private String strPesquisa;

    private Color darkred;
    private Color verydarkgray;

    private JPanel pnlCabecalho;
    private JScrollPane pnlCenter;
    private JPanel pnlConteudo;
    private JPanel pnlRodape;
    private JPanel pnlTitulo;

    private RFTable tblLocais;
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

    private RFMenuBar menuBar;
    private RFMenu menu;
    private RFMenuItem menuItem;

    private JPanel pnlNavBar;
    private JPanel pnlTopo;

    private Empresa empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
    private List<Local> listaLocal = localControl.getLocalDaEmpresa(empresa);

    public FrmLocalGerenciamento() throws IOException {
        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 36);
        } catch (Exception e) {
            e.printStackTrace();
        }

        darkred = new Color(155, 27, 48);
        verydarkgray = new Color(32, 35, 42);

        lblTitulo = new RFLabel("GERENCIAMENTO DE LOCAIS");
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

        tblLocais = new RFTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Permite editar todas menos a coluna ID
            }
        };

        dados = new DefaultTableModel();

        barraPesquisa = new RFBarraPesquisa();

        txtDesativar = new RFTextField();

        btnExcluir = new RFButton("Excluir local");
        btnDesativar = new RFButton("Excluir");
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
        navBar.getBotao("Local").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popup = new JPopupMenu();
                popup.setBackground(verydarkgray);

                JMenuItem opCadastrar = new JMenuItem("Cadastrar Local");
                opCadastrar.setBackground(verydarkgray);
                opCadastrar.setForeground(Color.WHITE);
                opCadastrar.setOpaque(true);

                opCadastrar.addActionListener(evt -> {
                    try {
                        new FrmLocalCadastro(); // sua tela de desativados
                        //dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                popup.add(opCadastrar);

                // Exibe o popup logo abaixo do botão
                popup.show(navBar.getBotao("Local"), 0, navBar.getBotao("Local").getHeight());
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

        navBar.getBotao("Produto").addActionListener(e -> {
            try {
                new FrmProdGerenciamento();
                dispose();
            } catch (JsonProcessingException ex) {
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

        setTitle("RFIND - Gerenciamento de Locais");
        setSize(960, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());

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
        //pnlRodape.add(btnExcluir, constraintsSecundarias);

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
        dados.addColumn("Descrição");
        dados.addColumn("Sensor");
        dados.setNumRows(0);

        for (Local local : listaLocal) {
            String sensorCodigo = local.getIdrfid() != null ? local.getIdrfid() : "Sem sensor";
            dados.addRow(new Object[]{
                    local.getId(),
                    local.getNome(),
                    local.getDescricao(),
                    sensorCodigo
            });
        }

        tblLocais.setModel(dados);
        tblLocais.repaint();
        pnlCenter.setViewportView(tblLocais);

        dados.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();

                if (!validarEdicaoTabela(row)) {
                    try {
                        atualizaTabelaCompleta();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return;
                }

                int id = (int) dados.getValueAt(row, 0);
                String nome = (String) dados.getValueAt(row, 1);
                String descricao = (String) dados.getValueAt(row, 2);
                String sensor = (String) dados.getValueAt(row, 3);

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Deseja realmente atualizar o local ID " + id + "?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    LocalUpdateDTO dto = new LocalUpdateDTO();
                    dto.setNome(nome);
                    dto.setDescricao(descricao);
                    dto.setSensor(sensor);

                    try {
                        localControl.updateLocal(id, dto);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Erro ao atualizar Local: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        atualizaTabelaCompleta();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void atualizaTabelaCompleta() throws IOException {
        Empresa empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
        listaLocal = localControl.getLocalDaEmpresa(empresa);

        dados.setNumRows(0);
        for (Local local : listaLocal) {
            String sensorCodigo = local.getIdrfid() != null ? local.getIdrfid() : "Sem sensor";
            dados.addRow(new Object[]{
                    local.getId(),
                    local.getNome(),
                    local.getDescricao(),
                    sensorCodigo
            });
        }
        tblLocais.setModel(dados);
    }

    private Sessao relembraSessao() {
        List<Sessao> sessao = Sessao.getSessao();
        Sessao s = sessao.getFirst();
        return s;
    }

    private void criaFrmPromptDesativacao() {
        frmPromptDesativar = new JFrame();
        frmPromptDesativar.setTitle("Digite o ID do local");
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
                    localControl.excluirLocal(Integer.parseInt(strPesquisa), empresa.getId());
                    RFMessageDialog.showMessageDialog(null, "Local excluído", "Aviso do sistema");
                    frmPromptDesativar.dispose();
                    atualizaTabelaCompleta();
                } catch (Exception ex) {
                    RFMessageDialog.showMessageDialog(null, "Local não encontrado", "Erro");
                }
            }
        });
    }

    private void pesquisarDinamicamente() {
        String texto = barraPesquisa.getTextoPesquisa();
        if (texto.isEmpty()) {
            try {
                atualizaTabelaCompleta();
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
                RFMessageDialog.showMessageDialog(null, "Erro ao carregar dados", "Erro");
                return;
            }
        }

        String filtro = barraPesquisa.getFiltroSelecionado();
        Integer id = null;
        String nome = null, descricao = null, sensor = null;

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
            case "Descrição":
                descricao = texto;
                break;
            case "Sensor":
                sensor = texto;
                break;
        }

        try {
            List<Local> resultados = localControl.buscarLocaisComFiltros(id, nome, descricao, sensor, empresa);

            dados.setRowCount(0);
            for (Local l : resultados) {
                dados.addRow(new Object[]{
                        l.getId(),
                        l.getNome(),
                        l.getDescricao(),
                        l.getIdrfid() != null ? l.getIdrfid() : "Sem sensor"
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            RFMessageDialog.showMessageDialog(null, "Erro ao pesquisar: " + ex.getMessage(), "Erro");
        }
    }

    private void configurarBarraPesquisa() {
        String[] filtrosLocal = {
                "ID",
                "Nome",
                "Descrição",
                "Sensor"
        };

        barraPesquisa.setFiltros(filtrosLocal);

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
            atualizaTabelaCompleta();
        } catch (IOException ex) {
            RFMessageDialog.showMessageDialog(null, "Erro ao recarregar dados", "Erro");
        }
    }

    private boolean validarEdicaoTabela(int row) {
        String nome = (String) dados.getValueAt(row, 1);
        String descricao = (String) dados.getValueAt(row, 2);

        if (nome == null || nome.trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(null, "Nome não pode ser vazio!", "Erro");
            return false;
        }

        if (nome.length() < 3 || nome.length() > 100) {
            RFMessageDialog.showMessageDialog(null, "Nome deve ter entre 3 e 100 caracteres!", "Erro");
            return false;
        }

        if (descricao == null || descricao.trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(null, "Descrição não pode ser vazia!", "Erro");
            return false;
        }

        if (descricao.length() > 200) {
            RFMessageDialog.showMessageDialog(null, "Descrição não pode ter mais de 200 caracteres!", "Erro");
            return false;
        }

        return true;
    }
}