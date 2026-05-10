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

public class FrmCadastro extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private EmpresaControl empresaControl;

    private final Color darkred;
    private final Color verydarkgray;
    private final JPanel pnlCabecalho;
    private final JPanel pnlConteudo;
    private final RFLabel lblTitulo;
    private final GridBagConstraints constraintsCabecalho;
    private final GridBagConstraints gridBagConstraints;
    private final Insets insetsCabecalho;
    private final Insets insets;
    private final Insets insetsMin;
    private final RFLabel lblCnpj;
    private final RFLabel lblNome;
    private final RFLabel lblSenha;
    private final RFLabel lblConfirmarSenha;
    private final RFLabel lblEmail;
    private final RFTextField txtCnpj;
    private final RFTextField txtNome;
    private final RFTextField txtEmail;
    private final RFPasswordField txtSenha;
    private final RFPasswordField txtConfirmarSenha;
    private final RFButton btnCadastrar;
    private JScrollPane pnlScroll;
    private final JScrollBar scrollBar;
    public FrmCadastro(){
        try{
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 48);
        }
        catch(Exception e){

        }

        empresaControl = new EmpresaControl();

        //sessão dos componentes
        darkred = new Color(155,27,48);//Color.decode("#9b1b30");
        verydarkgray = new Color(32,35,42);//Color.decode("#20232a");
        pnlCabecalho = new JPanel();
        pnlConteudo = new JPanel();
        lblTitulo = new RFLabel("CADASTRAR-SE");
        insetsCabecalho = new Insets(75,0,75,0);
        insets = new Insets(10,0,25,0);
        insetsMin = new Insets(5,0,10,0);
        constraintsCabecalho = new GridBagConstraints();
        gridBagConstraints = new GridBagConstraints();
        lblCnpj = new RFLabel("CNPJ da empresa");
        lblNome = new RFLabel("Nome da empresa");
        lblEmail = new RFLabel("Email da empresa");
        lblSenha = new RFLabel("Senha");
        lblConfirmarSenha = new RFLabel("Confirmar senha");
        txtCnpj = new RFTextField();
        txtNome = new RFTextField();
        txtEmail = new RFTextField();
        txtSenha = new RFPasswordField();
        txtConfirmarSenha = new RFPasswordField();
        btnCadastrar = new RFButton("Cadastrar-se");
        pnlScroll = new JScrollPane();
        scrollBar = pnlScroll.getVerticalScrollBar();
        pnlScroll.getVerticalScrollBar().setUI(new RFScrollBar());
        pnlScroll.getHorizontalScrollBar().setUI(new RFScrollBar());
        //fim da sessão

        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());

        //sessão dos setters
        setTitle("RFIND - Cadastrar-se");
        setSize(480,640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        pnlConteudo.setBackground(verydarkgray);
        //fim da sessão

        //sessão da mudança de componentes
        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setPreferredSize(new Dimension(640,150));
        pnlCabecalho.setLayout(new GridBagLayout());
        pnlConteudo.setLayout(new GridBagLayout());
        lblTitulo.setFont(lexendExa);
        scrollBar.setUnitIncrement(8);
        scrollBar.setBlockIncrement(50);
        //fim da sessão

        pnlCabecalho.setOpaque(false);
        pnlConteudo.setOpaque(false);

        // Scroll transparente
        pnlScroll.setOpaque(false);
        pnlScroll.getViewport().setOpaque(false);

        //sessão da adição de componentes;
        constraintsCabecalho.insets = insetsCabecalho;
        pnlCabecalho.add(lblTitulo,constraintsCabecalho);

        gridBagConstraints.insets = insetsMin;
        pnlConteudo.add(lblCnpj,gridBagConstraints);
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = insets;
        pnlConteudo.add(txtCnpj,gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = insetsMin;
        pnlConteudo.add(lblNome,gridBagConstraints);
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = insets;
        pnlConteudo.add(txtNome,gridBagConstraints);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = insetsMin;

        pnlConteudo.add(lblEmail,gridBagConstraints);
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = insets;
        pnlConteudo.add(txtEmail,gridBagConstraints);
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = insetsMin;
        pnlConteudo.add(lblSenha,gridBagConstraints);
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = insets;
        pnlConteudo.add(txtSenha,gridBagConstraints);
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = insetsMin;
        pnlConteudo.add(lblConfirmarSenha,gridBagConstraints);
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = insets;
        pnlConteudo.add(txtConfirmarSenha,gridBagConstraints);
        gridBagConstraints.gridy = 10;
        pnlConteudo.add(btnCadastrar,gridBagConstraints);

        pnlScroll.setViewportView(pnlConteudo);
        pnlScroll.setBorder(null);

        add(pnlCabecalho,BorderLayout.NORTH);
        add(pnlScroll,BorderLayout.CENTER);
        //fim da sessão

        //sessão dos listeners
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                dispose();
                SwingUtilities.invokeLater(FrmBoasVindas::new);
            }
        });
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Validação do CNPJ
                if (!validarCNPJ(txtCnpj.getText().trim())) {
                    RFMessageDialog.showMessageDialog(null, "CNPJ inválido! Deve conter 14 dígitos numéricos.", "Erro de Validação");
                    txtCnpj.requestFocus();
                    return;
                }

                // Validação do Nome
                if (txtNome.getText().trim().isEmpty()) {
                    RFMessageDialog.showMessageDialog(null, "O nome da empresa é obrigatório!", "Erro de Validação");
                    txtNome.requestFocus();
                    return;
                }

                if (txtNome.getText().trim().length() < 3 || txtNome.getText().trim().length() > 100) {
                    RFMessageDialog.showMessageDialog(null, "O nome da empresa deve ter entre 3 e 100 caracteres!", "Erro de Validação");
                    txtNome.requestFocus();
                    return;
                }

                // Validação do Email
                String email = txtEmail.getText().trim();
                if (email.isEmpty()) {
                    RFMessageDialog.showMessageDialog(null, "O email da empresa é obrigatório!", "Erro de Validação");
                    txtEmail.requestFocus();
                    return;
                }

                if (!validarEmail(email)) {
                    RFMessageDialog.showMessageDialog(null, "Email inválido! Digite um email válido.", "Erro de Validação");
                    txtEmail.requestFocus();
                    return;
                }

                // Validação da Senha
                String senha = String.valueOf(txtSenha.getPassword());
                if (senha.isEmpty()) {
                    RFMessageDialog.showMessageDialog(null, "A senha é obrigatória!", "Erro de Validação");
                    txtSenha.requestFocus();
                    return;
                }

                if (senha.length() < 8) {
                    RFMessageDialog.showMessageDialog(null, "A senha deve ter no mínimo 8 caracteres!", "Erro de Validação");
                    txtSenha.requestFocus();
                    return;
                }

                // Validação de Confirmação de Senha
                String confirmarSenha = String.valueOf(txtConfirmarSenha.getPassword());
                if (!senha.equals(confirmarSenha)) {
                    RFMessageDialog.showMessageDialog(null, "As senhas não coincidem!", "Erro de Validação");
                    txtConfirmarSenha.requestFocus();
                    return;
                }


                try{
                    if(String.valueOf(txtConfirmarSenha.getPassword()).equals(String.valueOf(txtSenha.getPassword()))){

                        String cnpj = txtCnpj.getText().replaceAll("[^0-9]", "");
                        Empresa empresa = new Empresa(cnpj, txtNome.getText(), txtEmail.getText(),String.valueOf(txtSenha.getPassword()));
                        empresaControl.insert(empresa);
                        txtCnpj.setText(null);
                        txtNome.setText(null);
                        txtEmail.setText(null);
                        txtSenha.setText(null);
                        txtConfirmarSenha.setText(null);
                        RFMessageDialog.showMessageDialog(null,"Cadastro concluído com êxito!","Sucesso");
                        dispose();
                        SwingUtilities.invokeLater(FrmBoasVindas::new);
                    }
                    else{
                        RFMessageDialog.showMessageDialog(null,"Senhas não são iguais!","Erro ao cadastrar");
                    }
                }
                catch(Exception ex){
                    RFMessageDialog.showMessageDialog(null,"Preencha todos os campos corretamente!","Erro ao cadastrar");
                }
            }
        });


        //fim da sessão

        setVisible(true);
    }

    private boolean validarCNPJ(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }
        return true;
    }

    private boolean validarEmail(String email) {
        // Verifica se não está vazio
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        email = email.trim();

        // Verifica comprimento máximo
        if (email.length() > 254) {
            return false;
        }

        // Expressão regular para validar formato básico de email
        String regexBasica = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(regexBasica)) {
            return false;
        }

        // Validações específicas para emails corporativos
        String dominio = email.substring(email.indexOf('@') + 1);

        // 2. Verifica se o domínio tem estrutura corporativa
        // Domínios corporativos geralmente não contêm números aleatórios
        if (dominio.matches(".*\\d{5,}.*")) {
            return false; // Muitos números no domínio (suspeito)
        }

        // 3. Verifica extensões comuns de domínios corporativos
        String[] extensoesValidas = {
                "com", "com.br", "br", "net", "net.br", "org", "org.br",
                "gov", "gov.br", "edu", "edu.br", "me", "io", "tech",
                "app", "cloud", "digital", "online", "site", "website"
        };

        String extensao = dominio.substring(dominio.lastIndexOf('.') + 1);
        boolean extensaoValida = false;
        for (String ext : extensoesValidas) {
            if (dominio.endsWith("." + ext) || dominio.equals(ext)) {
                extensaoValida = true;
                break;
            }
        }

        if (!extensaoValida) {
            return false; // Extensão de domínio não reconhecida
        }

        // 4. Verifica se não é um email temporário/disposável
        String[] servicosTemporarios = {
                "tempmail", "disposable", "throwaway", "10minutemail", "mailinator",
                "guerrillamail", "yopmail", "temp-mail", "fake-mail", "trashmail",
                "dispostable", "maildrop", "getnada", "tmpmail", "tempr",
                "quickmail", "throwawaymail", "incognito", "anonymous",
                "mailnesia", "jetable", "harakirimail", "mailmetrash",
                "33mail", "disposableemail", "tempinbox", "mintemail",
                "owleyes", "spamgourmet", "burnermail", "maildu",
                "freemail", "byom.de", "crazymailing", "dodgit",
                "emailias", "gishpuppy", "mailmoat", "mailnull",
                "mintemail", "mytrashmail", "nospamthanks", "spamcorptastic",
                "spamfour", "tempomail", "thankyou2010", "uroid",
                "wegwerfmail", "willselfdestruct", "zoemail"
        };

        for (String temp : servicosTemporarios) {
            if (dominio.contains(temp)) {
                return false; // Email temporário
            }
        }

        // 5. Verifica estrutura do nome de usuário (parte antes do @)
        String usuario = email.substring(0, email.indexOf('@'));

        if (usuario.length() < 2) {
            return false; // Nome de usuário muito curto
        }

        if (usuario.matches(".*[^A-Za-z0-9._-].*")) {
            return false; // Caracteres inválidos
        }

        // 6. Verifica se não começa ou termina com ponto ou hífen
        if (usuario.startsWith(".") || usuario.startsWith("-") ||
                usuario.endsWith(".") || usuario.endsWith("-")) {
            return false;
        }

        return true;
    }


}