package view;

import com.fasterxml.jackson.core.JsonProcessingException;
import control.EmpresaControl;
import control.LocalControl;
import model.Empresa;
import model.Sessao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FrmLocalAtualizacao extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private Color verydarkgray = new Color(32, 35, 42);
    private Color darkred = new Color(155, 27, 48);

    private JScrollPane pnlCenter;
    private JPanel pnlCabecalho;
    private JPanel pnlConteudo;

    private GridBagConstraints gbc = new GridBagConstraints();
    private GridBagConstraints gbcCabecalho = new GridBagConstraints();

    private Insets insetsCabecalho = new Insets(75, 0, 75, 0);
    private Insets insets = new Insets(10, 0, 25, 0);
    private Insets insetsMin = new Insets(5, 0, 10, 0);

    private RFLabel lblTitulo = new RFLabel("ATUALIZAR PRODUTO");
    private RFTextField txtId = new RFTextField();
    private RFTextField txtNome = new RFTextField();
    private RFTextField txtDescricao = new RFTextField();

    private RFButton btnAtualizar = new RFButton("Atualizar");

    private EmpresaControl empresaControl = new EmpresaControl();
    private LocalControl localControl = new LocalControl();

    private Sessao sessao;

    public FrmLocalAtualizacao() {
        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa).deriveFont(Font.PLAIN, 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sessao = relembrarSessao();

        // Configuração visual
        lblTitulo.setFont(lexendExa);
        pnlCabecalho = new JPanel(new GridBagLayout());
        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setPreferredSize(new Dimension(640, 150));
        gbcCabecalho.insets = insetsCabecalho;
        pnlCabecalho.add(lblTitulo, gbcCabecalho);

        pnlConteudo = new JPanel(new GridBagLayout());
        pnlConteudo.setBackground(verydarkgray);

        // Componentes
        int y = 0;
        addField("ID", txtId, y); y += 2;
        addField("Nome", txtNome, y); y += 2;
        addField("Descrição", txtDescricao, y); y += 2;

        gbc.gridy = y++;
        //pnlConteudo.add(btnAtualizar, gbc);

        pnlCenter = new JScrollPane(pnlConteudo);
        pnlCenter.getVerticalScrollBar().setUI(new RFScrollBar());
        pnlCenter.getHorizontalScrollBar().setUI(new RFScrollBar());

        setTitle("RFIND - Atualizar Local");
        setSize(480, 720);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(pnlCabecalho, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        btnAtualizar.addActionListener((ActionEvent e) -> {
            try {
                String nome = txtNome.getText().trim();
                String desc = txtDescricao.getText().trim();

                Empresa empresa = empresaControl.findCnpj(sessao.getCnpj());
                //localControl.updateLocal(Integer.parseInt(txtId.getText()), empresa);
                RFMessageDialog.showMessageDialog(null, "Local atualizado com sucesso!", "Sucesso");

                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                RFMessageDialog.showMessageDialog(null, "Preencha todos os campos corretamente!", "Erro");
            }
        });

        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    new FrmLocalGerenciamento().setVisible(true);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void addField(String label, JComponent field, int y) {
        gbc.gridy = y;
        gbc.insets = insetsMin;
        pnlConteudo.add(new RFLabel(label), gbc);

        gbc.gridy = y + 1;
        gbc.insets = insets;
        pnlConteudo.add(field, gbc);
    }

    private void addLabel(String text, int y) {
        gbc.gridy = y;
        gbc.insets = insetsMin;
        pnlConteudo.add(new RFLabel(text), gbc);
    }

    private Sessao relembrarSessao() {
        List<Sessao> sessao = Sessao.getSessao();
        return sessao.getFirst();
    }

    // Corrigido: Formatação correta para datas
    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String pattern = "yyyy-MM-dd";
        private final java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat(pattern);

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parse(text);
        }

        @Override
        public String valueToString(Object value) throws java.text.ParseException {
            if (value != null) {
                return dateFormatter.format(((java.util.Calendar) value).getTime());
            }
            return "";
        }
    }
}
