package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import control.EmpresaControl;
import control.FuncionarioControl;
import model.Empresa;
import model.Funcionario;
import model.Sessao;
import model.dto.FuncionarioUpdateDTO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FrmFuncGerenciamento extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private RFBarraPesquisa barraPesquisa;

    private JFrame frmPromptDesativar;
    private JFrame frmPromptPesquisa;

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

    private RFTable tblFuncionarios;
    private DefaultTableModel dados;

    private RFTextField txtPesquisa;
    private RFTextField txtDesativar;

    private RFButton btnAlterar;
    private RFButton btnExcluir;
    private RFButton btnPesquisar;
    private RFButton btnPesquisar2;
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
    private List<Funcionario> listaFuncAtivos = funcionarioControl.buscarAtivosPorEmpresa(empresa.getId());

    public FrmFuncGerenciamento() throws JsonProcessingException {
        try{
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 36);
        }
        catch(Exception e){

        }

        darkred = new Color(155,27,48);
        verydarkgray = new Color(32,35,42);

        lblTitulo = new RFLabel("FUNCIONÁRIOS ATIVOS");
        lblTitulo.setFont(lexendExa.deriveFont(Font.BOLD, 20));

        pnlTitulo = new JPanel();
        //pnlTitulo.setBackground(verydarkgray);
        pnlTitulo.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlTitulo.add(lblTitulo);

        pnlNavBar = new JPanel();
        pnlTopo = new JPanel();

        pnlCabecalho = new JPanel();
        pnlRodape = new JPanel();
        //pnlRodape.setBackground(verydarkgray);
        pnlRodape.setLayout(new GridBagLayout());
        pnlRodape.setPreferredSize(new Dimension(1280, 120)); // Aumente a altura se necessário
        pnlRodape.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Margem superior e inferior
        pnlCenter = new JScrollPane();
        //pnlCenter.setBackground(verydarkgray);
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margens: top, left, bottom, right
        pnlConteudo = new JPanel();

        pnlTitulo.setOpaque(false);
        pnlNavBar.setOpaque(false);
        pnlCabecalho.setOpaque(false);
        pnlRodape.setOpaque(false);
        pnlCenter.setOpaque(false);
        pnlCenter.getViewport().setOpaque(false);
        pnlConteudo.setOpaque(false);
        pnlTopo.setOpaque(false);

        tblFuncionarios = new RFTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Permite editar todas menos a coluna ID
            }
        };

        // Configura a tabela para ser transparente
        tblFuncionarios.setOpaque(false);
        tblFuncionarios.setBackground(new Color(0, 0, 0, 100)); // Fundo semi-transparente
        tblFuncionarios.setForeground(Color.WHITE); // Texto branco para contraste

        dados = new DefaultTableModel();

        barraPesquisa = new RFBarraPesquisa();

        txtPesquisa = new RFTextField();
        txtPesquisa.setOpaque(false);
        txtPesquisa.setBackground(new Color(0,0,0,0)); // Fundo totalmente transparente
        txtPesquisa.setBorder(BorderFactory.createEmptyBorder()); // Se não quiser borda

        txtDesativar = new RFTextField();

        btnAlterar = new RFButton("Atualizar funcionário");
        btnExcluir = new RFButton("Desativar funcionário");
        btnPesquisar = new RFButton("Pesquisar funcionário");
        btnPesquisar2 = new RFButton("Pesquisar");
        btnDesativar = new RFButton("Desativar");

        insetsCabecalho = new Insets(75,0,75,0);
        insetsRodape = new Insets(50,20,50,20);

        constraintsSecundarias = new GridBagConstraints();
        gridBagConstraints = new GridBagConstraints();
        //fim da sessão

        //sessão da mudança de componentes

        pnlTopo.setLayout(new BoxLayout(pnlTopo, BoxLayout.Y_AXIS));

        pnlNavBar.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        //pnlNavBar.setBackground(verydarkgray);

        //Menu de Navegação
        RFNavBar navBar = new RFNavBar(lexendExa, verydarkgray);
        //add(navBar, BorderLayout.NORTH);

        //comportamento dos botões
        navBar.getBotao("Funcionário").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popup = new JPopupMenu();
                popup.setBackground(verydarkgray);

                JMenuItem opDesativados = new JMenuItem("Funcionários Desativados");
                opDesativados.setBackground(verydarkgray);
                opDesativados.setForeground(Color.WHITE);
                opDesativados.setOpaque(true);

                JMenuItem opCadastrar = new JMenuItem("Cadastrar Funcionário");
                opCadastrar.setBackground(verydarkgray);
                opCadastrar.setForeground(Color.WHITE);
                opCadastrar.setOpaque(true);

                opDesativados.addActionListener(evt -> {
                    try {
                        new FrmFuncDesativados(); // abre tela de desativados
                        //dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                opCadastrar.addActionListener(evt -> {
                    try {
                        new FrmFuncCadastro(); // sua tela de desativados
                        //dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                popup.add(opDesativados);
                popup.add(opCadastrar);

                // Exibe o popup logo abaixo do botão
                popup.show(navBar.getBotao("Funcionário"), 0, navBar.getBotao("Funcionário").getHeight());
            }
        });

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


        //pnls
        pnlNavBar.add(navBar);

        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setLayout(new GridBagLayout());
        pnlCabecalho.setPreferredSize(new Dimension(1280,115));

        pnlCenter.getViewport().setBackground(verydarkgray);

        // Aplica o scrollbar personalizado
        JScrollBar verticalScrollBar = pnlCenter.getVerticalScrollBar();
        verticalScrollBar.setUI(new RFFinoScrollBarUI());
        verticalScrollBar.setPreferredSize(new Dimension(5, 0)); // Largura fina

        JScrollBar horizontalScrollBar = pnlCenter.getHorizontalScrollBar();
        horizontalScrollBar.setUI(new RFFinoScrollBarUI());
        horizontalScrollBar.setPreferredSize(new Dimension(0, 5)); // Altura fina

        pnlConteudo.setLayout(new GridBagLayout());

        pnlRodape.setBackground(verydarkgray);
        //fim da sessão

        //sessão dos setters
        setTitle("RFIND - Funcionários Ativos");
        setSize(960,640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(new BorderLayout());
        setContentPane(gradientPanel);

        inicializaTabela();
        configurarBarraPesquisa();
        //fim da sessão

        pnlTopo.add(pnlNavBar);
        pnlTopo.add(pnlTitulo);
        pnlTopo.add(pnlCabecalho);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        pnlCabecalho.add(barraPesquisa, gridBagConstraints);

        btnRecarregar = new RFButton("Recarregar tabela");

        //sessão da adição de componentes
        // Primeira linha de botões (superior)
        constraintsSecundarias.insets = new Insets(15, 20, 15, 20);
        constraintsSecundarias.gridx = 0;
        constraintsSecundarias.gridy = 0;
        //pnlRodape.add(btnAlterar, constraintsSecundarias);

        constraintsSecundarias.gridx = 0;
        pnlRodape.add(btnExcluir, constraintsSecundarias);

// Segunda linha de botões (inferior)
        constraintsSecundarias.gridx = 0;
        constraintsSecundarias.gridy = 0;
        //pnlRodape.add(btnPesquisar, constraintsSecundarias);

        constraintsSecundarias.gridx = 1;
        pnlRodape.add(btnRecarregar, constraintsSecundarias);

        pnlConteudo.add(pnlCenter);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(pnlTopo, BorderLayout.NORTH);
        mainPanel.add(pnlCenter, BorderLayout.CENTER);
        mainPanel.add(pnlRodape, BorderLayout.SOUTH);

        // Adicione o mainPanel ao gradientPanel (que é o content pane)
        gradientPanel.add(navBar, BorderLayout.NORTH);
        gradientPanel.add(mainPanel, BorderLayout.CENTER);

        //menuBar.add(menu);
        //menu.add(menuItem);
        //menu.add(menuItemLocal);
        //setJMenuBar(menuBar);
        //fim da sessão

        barraPesquisa.addPesquisaListener((texto, filtro) -> {
            pesquisarDinamicamente(); // Mantém sua lógica atual
        });


        btnRecarregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparPesquisa(); // Substitui a chamada direta a atualizaTabelaAtivos()
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(frmPromptDesativar == null){
                    criaFrmPromptDesativacao();
                }
                frmPromptDesativar.setVisible(true);
            }
        });
        //fim da sessão

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
        for(Funcionario funcionario : listaFuncAtivos){
            dados.addRow(new Object[]{funcionario.getId(),funcionario.getNome(),funcionario.getSobrenome(),funcionario.getSetor(), funcionario.getTagRfid()});
        }
        tblFuncionarios.setModel(dados);
        tblFuncionarios.repaint();
        pnlCenter.setViewportView(tblFuncionarios);

        dados.addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();

                if (!validarEdicaoTabela(row)) {
                    try {
                        atualizaTabelaAtivos(); // Recarrega os dados para restaurar o valor antigo
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                    return;
                }

                // Obtém os dados da linha editada
                int id = (int) dados.getValueAt(row, 0);
                String nome = (String) dados.getValueAt(row, 1);
                String sobrenome = (String) dados.getValueAt(row, 2);
                String setor = (String) dados.getValueAt(row, 3);
                String tag = (String) dados.getValueAt(row, 4);

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Deseja realmente atualizar o funcionário ID " + id + "?",
                        "Confirmação", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {

                    if(nome.isEmpty() || nome == null){
                        RFMessageDialog.showMessageDialog(null,"Nome não pode ser vazio!","Erro ao alterar dados");
                    }
                    FuncionarioUpdateDTO dto = new FuncionarioUpdateDTO();
                    dto.setNome(nome);
                    dto.setSobrenome(sobrenome);
                    dto.setSetor(setor);
                    dto.setTag(tag);

                    try {
                        funcionarioControl.atualizarFuncionario(id, dto);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Erro ao atualizar funcionário: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        atualizaTabelaAtivos(); // Recarrega os dados para restaurar o valor antigo
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

    }

    private void atualizaTabelaAtivos() throws JsonProcessingException {
        Empresa empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
        listaFuncAtivos = funcionarioControl.buscarAtivosPorEmpresa(empresa.getId());

        dados.setNumRows(0);
        for (Funcionario funcionario : listaFuncAtivos) {
            if (funcionario.getAtivado()) {
                dados.addRow(new Object[]{
                        funcionario.getId(),
                        funcionario.getNome(),
                        funcionario.getSobrenome(),
                        funcionario.getSetor(),
                        funcionario.getTagRfid()
                });
            }
        }

        tblFuncionarios.setModel(dados);
    }


    private Sessao relembraSessao(){
        List<Sessao> sessao = Sessao.getSessao();
        Sessao s = sessao.getFirst();
        return s;
    }

    private void criaFrmPromptDesativacao(){
        frmPromptDesativar = new JFrame();
        frmPromptDesativar.setTitle("Digite o ID do funcionário");
        frmPromptDesativar.setSize(450,250);
        frmPromptDesativar.getContentPane().setBackground(verydarkgray);
        frmPromptDesativar.setLayout(new GridBagLayout());
        frmPromptDesativar.setLocationRelativeTo(this);
        frmPromptDesativar.setResizable(false);
        frmPromptDesativar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(15,0,15,0);
        frmPromptDesativar.add(txtDesativar,gridBagConstraints);
        gridBagConstraints.gridy = 1;
        frmPromptDesativar.add(btnDesativar,gridBagConstraints);

        btnDesativar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Empresa empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
                    strPesquisa = txtDesativar.getText();
                    txtDesativar.setText(null);
                    //funcionarioControl.desativaFuncionario(Integer.parseInt(strPesquisa));
                    funcionarioControl.desativarFuncionario(Integer.parseInt(strPesquisa), empresa.getId());
                    RFMessageDialog.showMessageDialog(null,"Funcionário desativado","Aviso do sistema");
                    //frmPromptDesativar.dispose();
                    atualizaTabelaAtivos();
                }
                catch(Exception ex){
                    RFMessageDialog.showMessageDialog(null,"Funcionário não encontrado","Erro");
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
        String nome = null, sobrenome = null, setor = null, tag = null;

        switch (filtro) {
            case "ID":
                try {
                    id = Integer.parseInt(texto);
                } catch (NumberFormatException e) {
                    //RFMessageDialog.showMessageDialog(null, "ID inválido", "Erro");
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

            // Filtra apenas os ativos
            List<Funcionario> resultadosAtivos = new ArrayList<>();
            for (Funcionario f : resultados) {
                if (f.getAtivado()) {
                    resultadosAtivos.add(f);
                }
            }

            dados.setRowCount(0);
            for (Funcionario f : resultadosAtivos) {
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

        // Configura o listener da pesquisa
        barraPesquisa.addPesquisaListener((texto, filtro) -> {
            pesquisarDinamicamente();
        });

        // Configura o botão de pesquisa (opcional)
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

    private boolean validarEdicaoTabela(int row) {
        // Obtém os dados da linha editada
        String nome = (String) dados.getValueAt(row, 1);
        String sobrenome = (String) dados.getValueAt(row, 2);
        String setor = (String) dados.getValueAt(row, 3);
        String tag = (String) dados.getValueAt(row, 4);

        // Validação do nome
        if (nome == null || nome.trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(null, "Nome não pode ser vazio!", "Erro");
            return false;
        }

        if (nome.length() < 3 || nome.length() > 100) {
            RFMessageDialog.showMessageDialog(null, "Nome deve ter entre 3 e 100 caracteres!", "Erro");
            return false;
        }

        // Validação do sobrenome
        if (sobrenome == null || sobrenome.trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(null, "Sobrenome não pode ser vazio!", "Erro");
            return false;
        }

        if (sobrenome.length() < 3 || sobrenome.length() > 100) {
            RFMessageDialog.showMessageDialog(null, "Sobrenome deve ter entre 3 e 100 caracteres!", "Erro");
            return false;
        }

        // Validação do setor
        if (setor == null || setor.trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(null, "Setor não pode ser vazio!", "Erro");
            return false;
        }

        if (setor.length() > 50) {
            RFMessageDialog.showMessageDialog(null, "Setor não pode ter mais de 50 caracteres!", "Erro");
            return false;
        }

        // Validação da tag RFID
        if (!(tag != null) || tag.trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(null, "Tag RFID não pode ser vazia!", "Erro");
            return false;

        }

        return true;
    }
}