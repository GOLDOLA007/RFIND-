package view;

import com.fasterxml.jackson.core.JsonProcessingException;
import control.EmpresaControl;
import control.ProdutoControl;
import model.Empresa;
import model.Sessao;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class FrmProdAtualizacao extends JFrame {
    private JTextField txtDataFabVisual = new JTextField(10);
    private JTextField txtDataValVisual = new JTextField(10);

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
    private RFTextField txtPreco = new RFTextField();
    private RFTextField txtEstoque = new RFTextField();
    private RFTextField txtCategoria = new RFTextField();
    private RFTextField txtFornecedor = new RFTextField();
    //private JComboBox<TipoEstadoProduto> cmbEstadoProduto = new JComboBox<>(TipoEstadoProduto.values());

    private JDatePickerImpl datePickerFab;
    private JDatePickerImpl datePickerVal;

    private RFButton btnAtualizar = new RFButton("Atualizar");

    private EmpresaControl empresaControl = new EmpresaControl();
    private ProdutoControl produtoControl = new ProdutoControl();

    private Sessao sessao;

    public FrmProdAtualizacao() {
        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa).deriveFont(Font.PLAIN, 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sessao = relembrarSessao();

        // Calendário
        UtilDateModel modelFab = new UtilDateModel();
        UtilDateModel modelVal = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoje");
        p.put("text.month", "Mês");
        p.put("text.year", "Ano");
        JDatePanelImpl panelFab = new JDatePanelImpl(modelFab, p);
        JDatePanelImpl panelVal = new JDatePanelImpl(modelVal, p);
        datePickerFab = new JDatePickerImpl(panelFab, new FrmProdAtualizacao.DateLabelFormatter());
        datePickerVal = new JDatePickerImpl(panelVal, new FrmProdAtualizacao.DateLabelFormatter());

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
        addField("Preço", txtPreco, y); y += 2;
        addField("Estoque", txtEstoque, y); y += 2;
        addField("Categoria", txtCategoria, y); y += 2;
        addField("Fornecedor", txtFornecedor, y); y += 2;

        addLabel("Data de Fabricação", y++);
        gbc.gridy = y++;
        gbc.insets = insets;
        pnlConteudo.add(datePickerFab, gbc);

        addLabel("Data de Validade", y++);
        gbc.gridy = y++;
        pnlConteudo.add(datePickerVal, gbc);

        addLabel("Estado do Produto", y++);
        gbc.gridy = y++;
        //pnlConteudo.add(cmbEstadoProduto, gbc);

        gbc.gridy = y++;
        pnlConteudo.add(btnAtualizar, gbc);

        pnlCenter = new JScrollPane(pnlConteudo);
        pnlCenter.getVerticalScrollBar().setUI(new RFScrollBar());
        pnlCenter.getHorizontalScrollBar().setUI(new RFScrollBar());

        setTitle("RFIND - Atualizar Produto");
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
                double preco = Double.parseDouble(txtPreco.getText().trim());
                int estoque = Integer.parseInt(txtEstoque.getText().trim());
                String categoria = txtCategoria.getText().trim();
                String fornecedor = txtFornecedor.getText().trim();
                //TipoEstadoProduto estado = (TipoEstadoProduto) cmbEstadoProduto.getSelectedItem();

                java.util.Date selectedFab = (java.util.Date) datePickerFab.getModel().getValue();
                java.util.Date selectedVal = (java.util.Date) datePickerVal.getModel().getValue();

                Empresa empresa = empresaControl.findCnpj(sessao.getCnpj());
                produtoControl.updateProduto(Integer.parseInt(txtId.getText()), txtNome.getText(), txtCategoria.getText(), Double.parseDouble(txtPreco.getText()), Integer.parseInt(txtEstoque.getText()), txtDescricao.getText(),selectedFab, selectedVal, txtFornecedor.getText(), true);
                RFMessageDialog.showMessageDialog(null, "Produto atualizado com sucesso!", "Sucesso");

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
                    new FrmProdGerenciamento();
                } catch (JsonProcessingException ex) {
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
