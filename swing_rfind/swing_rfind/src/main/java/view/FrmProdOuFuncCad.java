package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

public class FrmProdOuFuncCad extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private final RFLabel lblTitulo;
    private final RFLabel lblPergunta;
    private final RFButton btnFuncionario;
    private final RFButton btnProduto;
    private final RFButton btnLocal; // Novo botão

    private final JPanel pnlCabecalho;
    private final JPanel pnlCenter;
    private final Color darkred;
    private final Color verydarkgray;

    private final GridBagConstraints gridBagConstraints;
    private final GridBagConstraints constraintsCabecalho;
    private final Insets cabecalhoInsets;
    private final Insets insets;

    public FrmProdOuFuncCad() {

        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 72);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Componentes visuais
        lblTitulo = new RFLabel("CADASTRAR");
        lblPergunta = new RFLabel("O que deseja cadastrar?");
        btnFuncionario = new RFButton("Funcionário");
        btnProduto = new RFButton("Produto");
        btnLocal = new RFButton("Local"); // Instância do novo botão

        pnlCabecalho = new JPanel(new GridBagLayout());
        pnlCenter = new JPanel(new GridBagLayout());
        darkred = new Color(155, 27, 48);
        verydarkgray = new Color(32, 35, 42);
        gridBagConstraints = new GridBagConstraints();
        constraintsCabecalho = new GridBagConstraints();
        cabecalhoInsets = new Insets(50, 0, 50, 0);
        insets = new Insets(20, 0, 20, 0);

        // Configurações da janela
        setSize(480, 640);
        setTitle("RFIND - Cadastro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());

        // Estilo e cores
        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setPreferredSize(new Dimension(640, 150));
        pnlCenter.setBackground(verydarkgray);

        // Título
        constraintsCabecalho.insets = cabecalhoInsets;
        lblTitulo.setFont(lexendExa.deriveFont(60f)); // 24f é o tamanho em pontos
        pnlCabecalho.add(lblTitulo, constraintsCabecalho);

        // Conteúdo central
        gridBagConstraints.insets = insets;
        gridBagConstraints.gridy = 0;
        pnlCenter.add(lblPergunta, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        pnlCenter.add(btnFuncionario, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        pnlCenter.add(btnProduto, gridBagConstraints);

        gridBagConstraints.gridy = 3;
        pnlCenter.add(btnLocal, gridBagConstraints); // Novo botão no layout

        // Adiciona aos painéis principais
        add(pnlCabecalho, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        // Ações dos botões
        btnFuncionario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(FrmFuncCadastro::new);
            }
        });

        btnProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(FrmProdCadastro::new);
            }
        });

        btnLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(FrmLocalCadastro::new); // Substitua por sua classe real
            }
        });

        pnlCabecalho.setOpaque(false);
        pnlCenter.setOpaque(false);
       // pnlTitulo.setOpaque(false);

        setVisible(true);
    }

    // Construtor auxiliar (não utilizado neste contexto)
    public FrmProdOuFuncCad(RFLabel lblTitulo, RFLabel lblPergunta, RFButton btnFuncionario, RFButton btnProduto, JPanel pnlCabecalho, JPanel pnlCenter, Color darkred, Color verydarkgray, GridBagConstraints gridBagConstraints, GridBagConstraints constraintsCabecalho, Insets cabecalhoInsets, Insets insets) {
        this.lblTitulo = lblTitulo;
        this.lblPergunta = lblPergunta;
        this.btnFuncionario = btnFuncionario;
        this.btnProduto = btnProduto;
        this.btnLocal = new RFButton("Local"); // Necessário para o outro construtor também
        this.pnlCabecalho = pnlCabecalho;
        this.pnlCenter = pnlCenter;
        this.darkred = darkred;
        this.verydarkgray = verydarkgray;
        this.gridBagConstraints = gridBagConstraints;
        this.constraintsCabecalho = constraintsCabecalho;
        this.cabecalhoInsets = cabecalhoInsets;
        this.insets = insets;
    }
}
