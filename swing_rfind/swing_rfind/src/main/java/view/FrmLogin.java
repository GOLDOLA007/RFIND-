package view;

import control.EmpresaControl;
import model.Empresa;
import model.Sessao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

public class FrmLogin extends JFrame {
    private EmpresaControl empresaControl;

    private InputStream fonteLexendExa;
    private Font lexendExa;

    private ImageIcon iconError;

    private final JPanel pnlCabecalho;
    private final RFLabel lblTitulo;
    private final GridBagConstraints cabecalhoConstraints;
    private final JPanel pnlCenter;
    private final GridBagConstraints gridBagConstraints;
    private RFTextField txtEmail;
    private RFPasswordField txtSenha;
    private final RFLabel lblEmail;
    private final RFLabel lblSenha;
    private final RFButton btnLogin;
    private final RFButton btnEsqueciMinhaSenha;
    private final Insets insets;
    private final Insets insetsMin;
    private final Insets insetsCabecalho;
    private final Color darkred;
    private final Color verydarkgray;
    public FrmLogin() {
        empresaControl = new EmpresaControl();

        try{
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 42);
        }
        catch(Exception e){

        }

        //sessão dos componentes
        iconError = new ImageIcon(getClass().getResource("/Images/Error.png"));

        pnlCabecalho = new JPanel();
        lblTitulo = new RFLabel("INICIAR SESSÃO");
        lblEmail = new RFLabel("Email da empresa");
        lblSenha = new RFLabel("Senha");
        pnlCenter = new JPanel();
        gridBagConstraints = new GridBagConstraints();
        cabecalhoConstraints = new GridBagConstraints();
        txtEmail = new RFTextField();
        txtSenha = new RFPasswordField();
        btnLogin = new RFButton("Iniciar sessão");
        btnEsqueciMinhaSenha = new RFButton("Esqueci minha senha");
        insets = new Insets(10,0,25,0);
        insetsMin = new Insets(5,0,10,0);
        insetsCabecalho = new Insets(75,0,75,0);
        darkred = Color.decode("#9b1b30");
        verydarkgray = Color.decode("#20232a");
        //fim da sessão

        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());

        //sessão dos setters
        setSize(480,640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("RFIND - Iniciar sessão");
        setResizable(false);
        setLayout(new BorderLayout());
        pnlCenter.setBackground(verydarkgray);
        pnlCenter.setOpaque(false);
        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());
        //fim da sessão

        pnlCabecalho.setOpaque(false);

        //sessão da mudança de componentes
        //Font quicksandatt = lblTitulo.getFont().deriveFont(Font.PLAIN,64);
        lblTitulo.setFont(lexendExa);
        pnlCabecalho.setPreferredSize(new Dimension(720,150));
        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setLayout(new GridBagLayout());
        pnlCenter.setLayout(new GridBagLayout());
        //fim da sessão

        //sessão da adição de componentes
        cabecalhoConstraints.insets = insetsCabecalho;
        gridBagConstraints.insets = insets;
        pnlCabecalho.add(lblTitulo,cabecalhoConstraints);
        gridBagConstraints.insets = insetsMin;
        pnlCenter.add(lblEmail,gridBagConstraints);
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = insets;
        pnlCenter.add(txtEmail,gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = insetsMin;
        pnlCenter.add(lblSenha,gridBagConstraints);
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = insets;
        pnlCenter.add(txtSenha,gridBagConstraints);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new Insets(5,0,15,0);
        pnlCenter.add(btnLogin,gridBagConstraints);
        gridBagConstraints.gridy = 5;
        //pnlCenter.add(btnEsqueciMinhaSenha,gridBagConstraints);

        add(pnlCabecalho,BorderLayout.NORTH);
        add(pnlCenter,BorderLayout.CENTER);
        //fim da sessão

        //sessão dos listeners
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                SwingUtilities.invokeLater(FrmBoasVindas::new);
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String email = txtEmail.getText().trim();
                    String senha = String.valueOf(txtSenha.getPassword()).trim();

                    if (email.isEmpty() || senha.isEmpty()) {
                        RFMessageDialog.showMessageDialog(null, "Preencha todos os campos!", "Erro ao iniciar sessão");
                        return;
                    }

                    Empresa emp = empresaControl.findEmail(email);

                    if (emp == null) {
                        RFMessageDialog.showMessageDialog(null, "Email não encontrado!", "Erro ao iniciar sessão");
                    } else if (!emp.getSenha().equals(senha)) {
                        RFMessageDialog.showMessageDialog(null, "Senha incorreta!", "Erro ao iniciar sessão");
                        txtSenha.setText("");
                    } else {
                        Sessao.setSessao(emp.getId(), emp.getCnpj(), emp.getNome(), emp.getSenha());
                        dispose();
                        SwingUtilities.invokeLater(() -> {

                            //new FrmMenu().setVisible(true);

                            try {
                                new FrmMenuInicial();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (FontFormatException ex) {
                                throw new RuntimeException(ex);
                            }

                        });
                    }

                } catch (Exception ex) {
                    ex.printStackTrace(); // log útil para depuração
                    RFMessageDialog.showMessageDialog(null, "Erro ao tentar iniciar sessão.", "Erro");
                }
            }
        });

        // Listener para o botão "Esqueci minha senha"
        btnEsqueciMinhaSenha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        new FrmEsqueciMinhaSenha();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        RFMessageDialog.showMessageDialog(null, "Erro ao abrir tela de recuperação de senha.", "Erro");
                    }
                });
                dispose();
            }
        });

        //fim da sessão

        pnlCabecalho.setOpaque(false);
        pnlCenter.setOpaque(false);
      //  pnlTitulo.setOpaque(false);

        setVisible(true);
    }
}