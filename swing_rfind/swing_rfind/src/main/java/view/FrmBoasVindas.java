package view;

import model.DAO.LeitorSerial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;

public class FrmBoasVindas extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private final JLabel lblRFI;
    private final JLabel lblN;
    private final JLabel lblD;
    private final RFLabel lblBoasVindas;
    private final RFButton btnLogin;
    private final RFButton btnCadastrar;
    private final JPanel pnlCabecalho;
    private final JPanel pnlCenter;

    private final Color darkred;
    private final Color verydarkgray;

    private final GridBagConstraints gridBagConstraints;
    private final GridBagConstraints constraintsCabecalho;
    private final Insets cabecalhoInsets;
    private final Insets insets;

    public FrmBoasVindas(){

        try{
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 72f);
        }
        catch(Exception e){
            e.printStackTrace();
            // fallback para fonte padrão
            lexendExa = new Font("SansSerif", Font.PLAIN, 72);
        }

        // Componentes do título dividido em 3 labels
        lblRFI = new JLabel("RFI");
        lblN = new JLabel("N");
        lblD = new JLabel("D");

        lblBoasVindas = new RFLabel("Seja bem-vindo! O que deseja fazer?");
        btnLogin = new RFButton("Iniciar sessão");
        btnCadastrar = new RFButton("Cadastrar-se");

        pnlCabecalho = new JPanel(new GridBagLayout());
        pnlCenter = new JPanel();

        pnlCabecalho.setOpaque(false);
        pnlCenter.setOpaque(false);

        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());

        darkred = new Color(155,27,48);
        verydarkgray = new Color(32,35,42);

        gridBagConstraints = new GridBagConstraints();
        constraintsCabecalho = new GridBagConstraints();
        cabecalhoInsets = new Insets(75,0,75,0);
        insets = new Insets(25,0,25,0);

        // Set font and color for the 3 parts of title
        lblRFI.setFont(lexendExa);
        lblRFI.setForeground(Color.WHITE);   // branco para "RFI"

        lblN.setFont(lexendExa);
        lblN.setForeground(darkred);         // vermelho para "N"

        lblD.setFont(lexendExa);
        lblD.setForeground(Color.WHITE);     // branco para "D"

        // Configurar painel do título (3 labels lado a lado)
        JPanel pnlTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlTitulo.setBackground(verydarkgray);
        pnlTitulo.add(lblRFI);
        pnlTitulo.add(lblN);
        pnlTitulo.add(lblD);
        pnlTitulo.setOpaque(false);

        // Configurações da janela
        setSize(480, 640); // 720,840
        setTitle("RFIND - Seja bem-vindo!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        pnlCenter.setBackground(verydarkgray);
        pnlCenter.setLayout(new GridBagLayout());

        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setPreferredSize(new Dimension(640, 150));

        // Adicionar o painel do título no cabeçalho com insets
        constraintsCabecalho.insets = cabecalhoInsets;
        pnlCabecalho.add(pnlTitulo, constraintsCabecalho);

        // Adicionar componentes ao centro
        gridBagConstraints.insets = insets;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlCenter.add(lblBoasVindas, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        pnlCenter.add(btnLogin, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        pnlCenter.add(btnCadastrar, gridBagConstraints);

        // Adicionar painéis ao frame
        add(pnlCabecalho, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        // Listeners dos botões
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(FrmLogin::new);
            }
        });

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(FrmCadastro::new);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args){
        //Process springProcess;
        System.setProperty("awt.useSystemAAFontSettings","on");
        SwingUtilities.invokeLater(FrmBoasVindas::new);
        //LeitorSerial leitor = new LeitorSerial();
        //leitor.iniciarLeitorSerial();
        /*try {
            String projectDir = System.getProperty("user.dir");
            String jarPath = projectDir + File.separator + "resources" + File.separator + "Archives" + File.separator + "api.jar";
            ProcessBuilder builder = new ProcessBuilder("java", "-jar", jarPath);

            builder.inheritIO();
            springProcess = builder.start();
            System.out.println("api iniciada");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
