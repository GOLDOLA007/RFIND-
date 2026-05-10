package view;

import control.EmpresaControl;
import control.ProdutoControl;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;

import org.jdatepicker.impl.*; // biblioteca do calendário
import java.util.Properties;

public class FrmProdCadastro extends JFrame {
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

    private RFLabel lblTitulo = new RFLabel("CADASTRAR PRODUTO");
    private RFTextField txtNome = new RFTextField();
    private RFTextField txtDescricao = new RFTextField();
    private RFTextField txtPreco = new RFTextField();
    private RFTextField txtEstoque = new RFTextField();
    private RFTextField txtCategoria = new RFTextField();
    private RFTextField txtFornecedor = new RFTextField();
    //private JComboBox<TipoEstadoProduto> cmbEstadoProduto = new JComboBox<>(TipoEstadoProduto.values());

    private JDatePickerImpl datePickerFab;
    private JDatePickerImpl datePickerVal;

    private RFButton btnCadastrar = new RFButton("Cadastrar");

    private EmpresaControl empresaControl = new EmpresaControl();
    private ProdutoControl produtoControl = new ProdutoControl();

    private Sessao sessao;

    public FrmProdCadastro() {
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
        datePickerFab = new JDatePickerImpl(panelFab, new DateLabelFormatter());
        datePickerVal = new JDatePickerImpl(panelVal, new DateLabelFormatter());

        // Configuração visual
        lblTitulo.setFont(lexendExa);
        pnlCabecalho = new JPanel(new GridBagLayout());
        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setPreferredSize(new Dimension(640, 150));
        gbcCabecalho.insets = insetsCabecalho;
        pnlCabecalho.add(lblTitulo, gbcCabecalho);

        // === GRADIENTE DE FUNDO ===
        setContentPane(new GradientPanel());      // <-- usa o painel com gradiente
        getContentPane().setLayout(new BorderLayout());

        pnlConteudo = new JPanel(new GridBagLayout());
        pnlConteudo.setBackground(verydarkgray);

        // Componentes
        int y = 0;
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

        //addLabel("Estado do Produto", y++);
        gbc.gridy = y++;
        //pnlConteudo.add(cmbEstadoProduto, gbc);

        gbc.gridy = y++;
        pnlConteudo.add(btnCadastrar, gbc);

        pnlCenter = new JScrollPane(pnlConteudo);
        pnlCenter.getVerticalScrollBar().setUI(new RFScrollBar());
        pnlCenter.getHorizontalScrollBar().setUI(new RFScrollBar());

        setTitle("RFIND - Cadastrar Produto");
        setSize(480, 720);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Deixe os painéis transparentes para que o gradiente apareça
        pnlCabecalho.setOpaque(false);
        pnlConteudo.setOpaque(false);
        pnlCenter.setOpaque(false);
        pnlCenter.getViewport().setOpaque(false);

        pnlCenter.setBorder(null);

        add(pnlCabecalho, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        btnCadastrar.addActionListener((ActionEvent e) -> {
            try {

                if (!validarCampos()) {
                    return;
                }

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

                Produto produto = new Produto();
                produto.setNome(nome);
                produto.setDescricao(desc);
                produto.setPreco(preco);
                produto.setQuantidadeEstoque(estoque);
                produto.setCategoria(categoria);
                produto.setFornecedor(fornecedor);
                produto.setEmpresa(empresa);
                produto.setAtivado(true);

                // Converter para java.sql.Date (que só armazena data, sem hora)
                if (selectedFab != null) {
                    produto.setDataFab(String.valueOf(new Date(selectedFab.getTime())));
                }
                if (selectedVal != null) {
                    produto.setDataValidade(String.valueOf(new Date(selectedVal.getTime())));
                }

                produtoControl.insert(produto);

                RFMessageDialog.showMessageDialog(null, "Produto cadastrado com sucesso!", "Sucesso");

                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                RFMessageDialog.showMessageDialog(null, "Preencha todos os campos corretamente!", "Erro");
            }
        });

        setVisible(true);
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

    private boolean validarCampos() {
        // Validação do nome
        if (txtNome.getText().trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(this, "O campo Nome é obrigatório", "Erro de Validação");
            txtNome.requestFocus();
            return false;
        }

        // Validação da descrição
        if (txtDescricao.getText().trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(this, "O campo Descrição é obrigatório", "Erro de Validação");
            txtDescricao.requestFocus();
            return false;
        }

        // Validação do preço
        try {
            double preco = Double.parseDouble(txtPreco.getText().trim());
            if (preco <= 0) {
                RFMessageDialog.showMessageDialog(this, "O preço deve ser maior que zero", "Erro de Validação");
                txtPreco.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            RFMessageDialog.showMessageDialog(this, "Preço inválido. Digite um valor numérico", "Erro de Validação");
            txtPreco.requestFocus();
            return false;
        }

        // Validação do estoque
        try {
            int estoque = Integer.parseInt(txtEstoque.getText().trim());
            if (estoque < 0) {
                RFMessageDialog.showMessageDialog(this, "O estoque não pode ser negativo", "Erro de Validação");
                txtEstoque.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            RFMessageDialog.showMessageDialog(this, "Estoque inválido. Digite um número inteiro", "Erro de Validação");
            txtEstoque.requestFocus();
            return false;
        }

        // Validação da categoria
        if (txtCategoria.getText().trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(this, "O campo Categoria é obrigatório", "Erro de Validação");
            txtCategoria.requestFocus();
            return false;
        }

        // Validação do fornecedor
        if (txtFornecedor.getText().trim().isEmpty()) {
            RFMessageDialog.showMessageDialog(this, "O campo Fornecedor é obrigatório", "Erro de Validação");
            txtFornecedor.requestFocus();
            return false;
        }

        // Validação das datas
        java.util.Date dataFab = (java.util.Date) datePickerFab.getModel().getValue();
        java.util.Date dataVal = (java.util.Date) datePickerVal.getModel().getValue();
        java.util.Date hoje = new java.util.Date();

        if (dataFab == null) {
            RFMessageDialog.showMessageDialog(this, "A data de fabricação é obrigatória", "Erro de Validação");
            datePickerFab.requestFocus();
            return false;
        }

        // Valida se a data de fabricação não é futura
        if (dataFab.after(hoje)) {
            RFMessageDialog.showMessageDialog(this,
                    "A data de fabricação não pode ser futura!",
                    "Erro de Validação");
            datePickerFab.requestFocus();
            return false;
        }

        if (dataVal == null) {
            RFMessageDialog.showMessageDialog(this, "A data de validade é obrigatória", "Erro de Validação");
            datePickerVal.requestFocus();
            return false;
        }

        if (dataVal.before(dataFab)) {
            RFMessageDialog.showMessageDialog(this, "Data de validade anterior à data de fabricação", "Erro de Validação");
            datePickerVal.requestFocus();
            return false;
        }

        return true;
    }

    // Corrigido: Formatação correta para datas
    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String pattern = "yyyy/MM/dd";
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
