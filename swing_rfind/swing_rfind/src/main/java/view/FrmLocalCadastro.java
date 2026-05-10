package view;

import control.EmpresaControl;
import control.LocalControl;
import control.SensorRfidControl;
import model.Empresa;
import model.Local;
import model.SensorRfid;
import model.Sessao;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FrmLocalCadastro extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private Color verydarkgray;
    private Color darkred;

    private JComboBox<String> cmbSensores;
    private SensorRfidControl sensorRfidControl = new SensorRfidControl();
    private static final Color COR_FUNDO = new Color(32, 35, 42); // Mesma cor do verydarkgray

    private JScrollPane pnlCenter;
    private JPanel pnlCabecalho;
    private JPanel pnlConteudo;

    private GridBagConstraints constraintsCabecalho;
    private GridBagConstraints gridBagConstraints;

    private Insets insetsCabecalho;
    private Insets insets;
    private Insets insetsMin;

    private RFLabel lblTitulo;
    private RFLabel lblNome;
    private RFLabel lblDescricao;
    private RFLabel lblIdSensor;

    private RFTextField txtNome;
    private RFTextField txtDescricao;
    private RFTextField txtIdSensor;

    private RFButton btnCadastrar;

    private JScrollBar scrollBar;

    private EmpresaControl empresaControl;
    private LocalControl localControl;

    private Sessao sessao;

    public FrmLocalCadastro() {
        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa);
            lexendExa = lexendExa.deriveFont(Font.PLAIN, 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        empresaControl = new EmpresaControl();
        localControl = new LocalControl();
        sessao = relembrarSessao();

        verydarkgray = new Color(32, 35, 42);
        darkred = new Color(155, 27, 48);

        lblTitulo = new RFLabel("CADASTRAR LOCAL");
        lblNome = new RFLabel("Nome do local");
        lblDescricao = new RFLabel("Descrição do local");
        lblIdSensor = new RFLabel("Sensor");

        txtNome = new RFTextField();
        txtDescricao = new RFTextField();
        //txtIdSensor = new RFTextField();

        // Inicializa a combobox vazia
        cmbSensores = criarComboBoxSensores(new String[0]);
        carregarSensoresNaoVinculados(); // Carrega os sensores disponíveis

        btnCadastrar = new RFButton("Cadastrar");

        pnlCabecalho = new JPanel(new GridBagLayout());
        pnlCenter = new JScrollPane();
        pnlConteudo = new JPanel(new GridBagLayout());

        gridBagConstraints = new GridBagConstraints();
        constraintsCabecalho = new GridBagConstraints();

        insetsCabecalho = new Insets(75, 0, 75, 0);
        insets = new Insets(10, 0, 25, 0);
        insetsMin = new Insets(5, 0, 10, 0);

        scrollBar = pnlCenter.getVerticalScrollBar();

        // === GRADIENTE DE FUNDO ===
        setContentPane(new GradientPanel());      // <-- usa o painel com gradiente
        getContentPane().setLayout(new BorderLayout());

        // Deixe os painéis transparentes para que o gradiente apareça
        pnlCabecalho.setOpaque(false);
        pnlConteudo.setOpaque(false);
        pnlCenter.setOpaque(false);
        pnlCenter.getViewport().setOpaque(false);
        // ===========================

        pnlCenter.setBorder(null);

        lblTitulo.setFont(lexendExa);

        pnlCabecalho.setPreferredSize(new Dimension(640, 150));
        pnlCabecalho.setBackground(verydarkgray);
        pnlConteudo.setBackground(verydarkgray);

        pnlCenter.getVerticalScrollBar().setUI(new RFScrollBar());
        pnlCenter.getHorizontalScrollBar().setUI(new RFScrollBar());

        scrollBar.setUnitIncrement(8);
        scrollBar.setBlockIncrement(50);

        setTitle("RFIND - Cadastrar local");
        setSize(480, 640);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        constraintsCabecalho.insets = insetsCabecalho;
        pnlCabecalho.add(lblTitulo, constraintsCabecalho);

        gridBagConstraints.insets = insetsMin;
        pnlConteudo.add(lblNome, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = insets;
        pnlConteudo.add(txtNome, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = insetsMin;
        pnlConteudo.add(lblDescricao, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = insets;
        pnlConteudo.add(txtDescricao, gridBagConstraints);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = insetsMin;
        pnlConteudo.add(lblIdSensor, gridBagConstraints);
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = insets;
        pnlConteudo.add(cmbSensores, gridBagConstraints);
        gridBagConstraints.gridy = 6;
        pnlConteudo.add(btnCadastrar, gridBagConstraints);

        pnlCenter.setViewportView(pnlConteudo);

        add(pnlCabecalho, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!validarCampos()) {
                        return;
                    }

                    // Verificação adicional para garantir que não estamos usando a mensagem placeholder
                    if (!cmbSensores.isEnabled() ||
                            cmbSensores.getSelectedItem().toString().startsWith("Todos os sensores") ||
                            cmbSensores.getSelectedItem().toString().startsWith("Erro ao carregar")) {
                        return;
                    }

                    Empresa empresa = empresaControl.findCnpj(sessao.getCnpj());
                    String sensorSelecionado = (String) cmbSensores.getSelectedItem();

                    Local local = new Local(txtNome.getText(), txtDescricao.getText(), sensorSelecionado, empresa);
                    localControl.cadastrarLocal(local);

                    RFMessageDialog.showMessageDialog(null,
                            "Local cadastrado com sucesso!",
                            "Aviso do sistema");

                    // Limpa os campos e recarrega a lista
                    txtNome.setText("");
                    txtDescricao.setText("");
                    carregarSensoresNaoVinculados();

                } catch (Exception ex) {
                    RFMessageDialog.showMessageDialog(null,
                            "Erro ao cadastrar local: " + ex.getMessage(),
                            "Erro");
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    private Sessao relembrarSessao() {
        List<Sessao> sessao = Sessao.getSessao();
        return sessao.getFirst();
    }

    private JComboBox<String> criarComboBoxSensores(String[] sensores) {
        // Cria um modelo com os itens em negrito
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String sensor : sensores) {
            model.addElement(sensor);
        }

        JComboBox<String> comboBox = new JComboBox<String>(model) {
            // Cria borda arredondada
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                g2.dispose();
            }

            // Renderizador personalizado para itens em negrito
            @Override
            public void setRenderer(ListCellRenderer<? super String> renderer) {
                super.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value,
                                                                  int index, boolean isSelected,
                                                                  boolean cellHasFocus) {
                        JLabel label = (JLabel) super.getListCellRendererComponent(
                                list, value, index, isSelected, cellHasFocus);

                        label.setFont(label.getFont().deriveFont(Font.BOLD));

                        // Customização para estado desativado
                        if (!list.isEnabled()) {
                            label.setForeground(new Color(150, 150, 150));
                            label.setBackground(new Color(60, 63, 65));
                        } else {
                            label.setForeground(isSelected ? Color.WHITE : Color.WHITE);
                            label.setBackground(isSelected ? new Color(75, 75, 75) : COR_FUNDO);
                        }

                        return label;
                    }
                });
            }
        };

        // Personaliza a UI do ComboBox
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼") {
                    @Override
                    public boolean isEnabled() {
                        return comboBox.isEnabled();
                    }
                };
                button.setForeground(Color.WHITE);
                button.setBackground(comboBox.isEnabled() ? COR_FUNDO : new Color(60, 63, 65));
                button.setBorder(null);
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                button.setFont(new Font("Dialog", Font.BOLD, 14));
                return button;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = new BasicComboPopup(comboBox) {
                    @Override
                    protected void configurePopup() {
                        super.configurePopup();
                        setBorder(BorderFactory.createLineBorder(Color.WHITE));
                        setOpaque(false);
                    }

                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane scroller = new JScrollPane(list);
                        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                        scroller.getViewport().setOpaque(false);
                        scroller.setOpaque(false);
                        return scroller;
                    }
                };

                // Personaliza a lista de opções
                popup.getList().setBackground(COR_FUNDO);
                popup.getList().setForeground(Color.WHITE);
                popup.getList().setSelectionBackground(new Color(75, 75, 75));
                popup.getList().setSelectionForeground(Color.WHITE);
                popup.getList().setFont(new Font("SansSerif", Font.BOLD, 14));

                return popup;
            }
        });

        // Configurações básicas
        comboBox.setBackground(COR_FUNDO);
        comboBox.setForeground(Color.WHITE);
        comboBox.setFocusable(false);
        comboBox.setOpaque(false);
        comboBox.setFont(new Font("SansSerif", Font.BOLD, 14));
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        // Listener para manter o estilo quando desativado
        comboBox.addPropertyChangeListener("enabled", evt -> {
            if (!comboBox.isEnabled()) {
                comboBox.setForeground(new Color(150, 150, 150));
                comboBox.setBackground(COR_FUNDO); // Cor escura
            } else {
                comboBox.setForeground(Color.WHITE);
                comboBox.setBackground(COR_FUNDO);
            }
        });

        return comboBox;
    }
    private void carregarSensoresNaoVinculados() {
        try {
            Empresa empresa = empresaControl.findCnpj(sessao.getCnpj());
            List<SensorRfid> sensores = sensorRfidControl.getNaoVinculados(empresa);

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            if (sensores.isEmpty()) {
                // Adiciona mensagem quando não há sensores disponíveis
                model.addElement("Todos os sensores já estão vinculados a um local");
                cmbSensores.setModel(model);
                cmbSensores.setEnabled(false);

                // Estilização adicional para o estado desativado
                cmbSensores.setForeground(new Color(150, 150, 150)); // Texto cinza
                cmbSensores.setBackground(COR_FUNDO); // Fundo mais escuro
            } else {
                // Carrega os sensores disponíveis
                String[] sensoresArray = sensores.stream()
                        .map(SensorRfid::getNome)
                        .toArray(String[]::new);

                for (String sensor : sensoresArray) {
                    model.addElement(sensor);
                }


                cmbSensores.setModel(model);
                cmbSensores.setEnabled(true);

                // Restaura a estilização padrão
                cmbSensores.setForeground(Color.WHITE);
                cmbSensores.setBackground(COR_FUNDO);
            }

        } catch (IOException e) {
            RFMessageDialog.showMessageDialog(this,
                    "Erro ao carregar sensores: " + e.getMessage(),
                    "Erro de Conexão");

            // Mostra mensagem de erro na própria combobox
            DefaultComboBoxModel<String> errorModel = new DefaultComboBoxModel<>();
            errorModel.addElement("Erro ao carregar sensores");
            cmbSensores.setModel(errorModel);
            cmbSensores.setEnabled(false);
            cmbSensores.setForeground(Color.RED);
        }
    }

    private boolean validarCampos() {
        // Validação básica de campos obrigatórios
        if (txtNome.getText().isEmpty() ||
                txtDescricao.getText().isEmpty() ||
                !cmbSensores.isEnabled() ||  // Verifica se a combobox está desativada
                cmbSensores.getSelectedItem() == null ||
                cmbSensores.getSelectedItem().toString().startsWith("Todos os sensores") ||
                cmbSensores.getSelectedItem().toString().startsWith("Erro ao carregar")) {

            if (!cmbSensores.isEnabled()) {
                RFMessageDialog.showMessageDialog(null,
                        "Não há sensores disponíveis para vincular!",
                        "Erro");
            } else {
                RFMessageDialog.showMessageDialog(null,
                        "Todos os campos são obrigatórios!",
                        "Erro");
            }
            return false;
        }

        // Validações específicas
        String nome = txtNome.getText().trim();
        if (nome.length() < 3 || nome.length() > 100) {
            RFMessageDialog.showMessageDialog(null, "O nome deve ter entre 3 e 100 caracteres!", "Erro");
            return false;
        }

        String descricao = txtDescricao.getText().trim();
        if (descricao.length() > 255) {
            RFMessageDialog.showMessageDialog(null, "A descrição não pode ultrapassar 255 caracteres!", "Erro");
            return false;
        }

        //String idSensor = txtIdSensor.getText().trim();
        /*
        if (!idSensor.matches("[a-zA-Z0-9-]{10,36}")) {
            RFMessageDialog.showMessageDialog(null, "ID do sensor inválido! Deve ser um código alfanumérico.", "Erro");
            return false;
        }*/

        /*
        if(idSensor.isEmpty()){
            RFMessageDialog.showMessageDialog(null, "ID do sensor não pode ser vazio.", "Erro");
            return false;
        }*/

        return true;
    }


}
