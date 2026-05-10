package view;

import control.EmpresaControl;
import model.Empresa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;

public class FrmEsqueciMinhaSenha extends JFrame {
    private EmpresaControl empresaControl;

    private InputStream fonteLexendExa;
    private Font lexendExa;

    private final JPanel pnlCabecalho;
    private final RFLabel lblTitulo;
    private final GridBagConstraints cabecalhoConstraints;
    private final JPanel pnlCenter;
    private final GridBagConstraints gridBagConstraints;
    private RFTextField txtEmail;
    private final RFLabel lblEmail;
    private final RFButton btnRecuperarSenha;
    private final Insets insets;
    private final Insets insetsMin;
    private final Insets insetsCabecalho;
    private final Color verydarkgray;

    public FrmEsqueciMinhaSenha() {
        empresaControl = new EmpresaControl();

        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 36);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // sessão dos componentes
        pnlCabecalho = new JPanel();
        lblTitulo = new RFLabel("RECUPERAR SENHA");
        lblEmail = new RFLabel("Email da empresa");
        pnlCenter = new JPanel();
        gridBagConstraints = new GridBagConstraints();
        cabecalhoConstraints = new GridBagConstraints();
        txtEmail = new RFTextField();
        btnRecuperarSenha = new RFButton("Recuperar Senha");
        insets = new Insets(10, 0, 25, 0);
        insetsMin = new Insets(5, 0, 10, 0);
        insetsCabecalho = new Insets(75, 0, 75, 0);
        verydarkgray = Color.decode("#20232a");
        // fim da sessão

        // sessão dos setters
        setSize(480, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("RFIND - Recuperar Senha");
        setResizable(false);
        setLayout(new BorderLayout());
        pnlCenter.setBackground(verydarkgray);
        // fim da sessão

        // sessão da mudança de componentes
        lblTitulo.setFont(lexendExa);
        pnlCabecalho.setPreferredSize(new Dimension(720, 150));
        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setLayout(new GridBagLayout());
        pnlCenter.setLayout(new GridBagLayout());
        // fim da sessão

        // sessão da adição de componentes
        cabecalhoConstraints.insets = insetsCabecalho;
        gridBagConstraints.insets = insets;
        pnlCabecalho.add(lblTitulo, cabecalhoConstraints);

        gridBagConstraints.insets = insetsMin;
        gridBagConstraints.gridy = 0;
        pnlCenter.add(lblEmail, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = insets;
        pnlCenter.add(txtEmail, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(25, 0, 15, 0);
        pnlCenter.add(btnRecuperarSenha, gridBagConstraints);

        add(pnlCabecalho, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
        // fim da sessão

        // sessão dos listeners
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                SwingUtilities.invokeLater(FrmLogin::new);
            }
        });

        btnRecuperarSenha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // fim da sessão

        pnlCabecalho.setOpaque(false);
        pnlCenter.setOpaque(false);
        //pnlTitulo.setOpaque(false);

        setVisible(true);
    }
}