package view;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

public class FrmProdOuFuncGer extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private final RFLabel lblTitulo;
    private final RFLabel lblPergunta;
    private final RFButton btnFuncionario;
    private final RFButton btnProduto;
    private final JPanel pnlCabecalho;
    private final JPanel pnlCenter;
    private final Color darkred;
    private final Color verydarkgray;

    private final GridBagConstraints gridBagConstraints;
    private final GridBagConstraints constraintsCabecalho;
    private final Insets cabecalhoInsets;
    private final Insets insets;

    public FrmProdOuFuncGer(){

        try{
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 72);
        }
        catch(Exception e){

        }

        // Componentes visuais
        lblTitulo = new RFLabel("GERENCIAR");
        lblPergunta = new RFLabel("O que deseja gerenciar?");
        btnFuncionario = new RFButton("Funcionário");
        btnProduto = new RFButton("Produto");
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
        setTitle("RFIND - Gerenciamento");
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
        lblTitulo.setFont(lexendExa.deriveFont(60f));
        pnlCabecalho.add(lblTitulo, constraintsCabecalho);

        // Conteúdo central
        gridBagConstraints.insets = insets;
        pnlCenter.add(lblPergunta, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        pnlCenter.add(btnFuncionario, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        pnlCenter.add(btnProduto, gridBagConstraints);

        // Adiciona aos painéis principais
        add(pnlCabecalho, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        btnFuncionario.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(() -> {
                    try {
                        new FrmFuncGerenciamento();
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        });

        btnProduto.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                SwingUtilities.invokeLater(() -> {
                    try {
                        new FrmProdGerenciamento();
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        });

        pnlCabecalho.setOpaque(false);
        pnlCenter.setOpaque(false);
      //  pnlTitulo.setOpaque(false);

        setVisible(true);
    }



    public FrmProdOuFuncGer(RFLabel lblTitulo, RFLabel lblPergunta, RFButton btnFuncionario, RFButton btnProduto, JPanel pnlCabecalho, JPanel pnlCenter, Color darkred, Color verydarkgray, GridBagConstraints gridBagConstraints, GridBagConstraints constraintsCabecalho, Insets cabecalhoInsets, Insets insets) {
        this.lblTitulo = lblTitulo;
        this.lblPergunta = lblPergunta;
        this.btnFuncionario = btnFuncionario;
        this.btnProduto = btnProduto;
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
