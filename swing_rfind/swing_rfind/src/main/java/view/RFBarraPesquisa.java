package view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Componente personalizado de barra de pesquisa com filtros e estilo moderno
 */
public class RFBarraPesquisa extends JPanel {
    private boolean programmaticChange = false;
    // Cores personalizadas
    private static final Color COR_FUNDO = new Color(32,35,42);

    //private Color verydarkgray = new Color(32,35,42);

    private static final Color COR_CAMPO = new Color(55, 55, 55);
    private static final Color COR_BOTOES = new Color(75, 75, 75);
    private static final Color COR_TEXTO = Color.WHITE;
    private static final Color COR_PLACEHOLDER = new Color(150, 150, 150);

    // Componentes UI
    private JTextField campoPesquisa;
    private JComboBox<String> filtroComboBox;
    private JButton botaoPesquisar;

    private boolean pesquisaDinamicaAtivada = true;

    /**
     * Constrói a barra de pesquisa com estilo moderno
     */
    public RFBarraPesquisa() {
        initComponents();
        configurarComportamento();
    }

    /**
     * Inicializa os componentes da interface
     */
    private void initComponents() {
        setLayout(new BorderLayout(2, 0));
        setBackground(COR_FUNDO);
        setOpaque(false); // Importante para o efeito de borda arredondada

        // Configuração do campo de pesquisa
        campoPesquisa = criarCampoPesquisa();

        // Configuração do combo box de filtros
        String[] filtros = {"ID", "Nome/Sobrenome", "Setor", "TAG/Cartão"};
        filtroComboBox = criarComboBoxFiltros(filtros);

        // Configuração do botão de pesquisa
        botaoPesquisar = criarBotaoPesquisa();

        // Adiciona componentes ao painel
        add(filtroComboBox, BorderLayout.EAST);
        add(campoPesquisa, BorderLayout.CENTER);
        add(botaoPesquisar, BorderLayout.WEST);

        /*
        // Cria borda arredondada branca para o painel principal
        setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(x, y, width-1, height-1, 10, 10); // 10px de arredondamento
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(5, 10, 5, 10); // Margem interna
            }
        });*/
        setBorder(null);

        setPreferredSize(new Dimension(425, 45));
    }

    /**
     * Cria e configura o campo de pesquisa
     */
    private JTextField criarCampoPesquisa() {
        JTextField campo = new JTextField();
        campo.setOpaque(false);
        campo.setBackground(new Color(0,0,0));
        campo.setForeground(COR_PLACEHOLDER);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Aumenta o tamanho da fonte
        campo.setBorder(null);
        return campo;
    }

    /**
     * Cria e configura o combo box de filtros
     */
    private JComboBox<String> criarComboBoxFiltros(String[] filtros) {
        // Cria um modelo com os itens em negrito
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String filtro : filtros) {
            model.addElement(filtro);
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
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                  boolean isSelected, boolean cellHasFocus) {
                        JLabel label = (JLabel) super.getListCellRendererComponent(
                                list, value, index, isSelected, cellHasFocus);
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                        return label;
                    }
                });
            }
        };

        // Personaliza a UI do ComboBox
        comboBox.setUI(new BasicComboBoxUI() {

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                // Não pinta o fundo – evita o retângulo preto
            }

            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setOpaque(false);
                button.setForeground(Color.WHITE);
                button.setBackground(new Color(0,0,0));
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

                // Personaliza a lista de opções com texto em negrito
                popup.setOpaque(false);
                //popup.getList().setOpaque(false);
                popup.getList().setBackground(new Color(0,0,0));
                popup.getList().setForeground(Color.WHITE);
                popup.getList().setSelectionBackground(new Color(75, 75, 75));
                popup.getList().setSelectionForeground(Color.WHITE);
                popup.getList().setFont(new Font("SansSerif", Font.BOLD, 14)); // Fonte em negrito

                return popup;
            }
        });

        // Configurações básicas
        comboBox.setOpaque(false);
        comboBox.setBackground(new Color(0,0,0, 0));
        comboBox.setBorder(null);
        comboBox.setForeground(Color.WHITE);
        comboBox.setFocusable(false);
        comboBox.setFont(new Font("SansSerif", Font.BOLD, 14)); // Fonte em negrito

        // Margem interna
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        return comboBox;
    }


    /**
     * Cria e configura o botão de pesquisa
     */
    private JButton criarBotaoPesquisa() {
        JButton botao = new JButton("🔍");
        botao.setOpaque(false);
        botao.setContentAreaFilled(false);
        botao.setFocusPainted(false);
        botao.setBackground(new Color(0,0,0));
        botao.setForeground(COR_TEXTO);
        botao.setBorder(null);
        botao.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Aumenta o tamanho da lupa
        botao.setPreferredSize(new Dimension(40, 40)); // Tamanho fixo para o botão
        return botao;
    }

    /**
     * Configura os comportamentos dinâmicos dos componentes
     */
    private void configurarComportamento() {
        atualizarPlaceholder();

        filtroComboBox.addActionListener(e -> {
            // Só atualiza o placeholder se o campo não tiver foco OU estiver vazio
            if (!campoPesquisa.hasFocus() || campoPesquisa.getText().isEmpty()) {
                atualizarPlaceholder();
            }
        });

        campoPesquisa.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholder()) {
                    campoPesquisa.setText("");
                    campoPesquisa.setForeground(COR_TEXTO);
                    campoPesquisa.setFont(new Font("SansSerif", Font.PLAIN, 14));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campoPesquisa.getText().isEmpty()) {
                    atualizarPlaceholder();
                }
            }
        });
    }

    // ========== MÉTODOS PÚBLICOS ========== //

    public void ativarPesquisaDinamica(boolean ativar) {
        this.pesquisaDinamicaAtivada = ativar;
    }

    public void addFiltroChangeListener(Runnable action) {
        filtroComboBox.addActionListener(e -> {
            atualizarPlaceholder();
            action.run(); // Executa a ação quando o filtro muda
        });
    }

    public void trocarFiltroComValidacao(String novoFiltro) {
        // Verifica se há texto de pesquisa ativo
        if (!getTextoPesquisa().isEmpty()) {
            limparPesquisa();
        }

        // Atualiza o filtro e placeholder
        filtroComboBox.setSelectedItem(novoFiltro);
        atualizarPlaceholder();
    }

    public void limparPesquisa() {
        programmaticChange = true;
        try {
            campoPesquisa.setText("");
            atualizarPlaceholder();
        } finally {
            programmaticChange = false;
        }
    }

    public void setFiltros(String[] novosFiltros) {
        filtroComboBox.setModel(new DefaultComboBoxModel<>(novosFiltros));
    }

    public String getTextoPesquisa() {
        return isPlaceholder() ? "" : campoPesquisa.getText().trim();
    }

    public String getFiltroSelecionado() {
        return (String) filtroComboBox.getSelectedItem();
    }

    public JButton getBotaoPesquisar() {
        return botaoPesquisar;
    }

    public JTextField getTextField() {
        return campoPesquisa;
    }

    public void addPesquisaListener(DocumentListener listener) {
        campoPesquisa.getDocument().addDocumentListener(listener);
    }

    public void addFiltroListener(ActionListener listener) {
        filtroComboBox.addActionListener(listener);
    }

    /**
     * Adiciona um listener personalizado para eventos de pesquisa
     */
    public void addPesquisaListener(PesquisaListener listener) {
        campoPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!programmaticChange && pesquisaDinamicaAtivada) {
                    listener.onPesquisa(getTextoPesquisa(), getFiltroSelecionado());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!programmaticChange && pesquisaDinamicaAtivada) {
                    listener.onPesquisa(getTextoPesquisa(), getFiltroSelecionado());
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!programmaticChange && pesquisaDinamicaAtivada) {
                    listener.onPesquisa(getTextoPesquisa(), getFiltroSelecionado());
                }
            }
        });

        botaoPesquisar.addActionListener(e ->
                listener.onPesquisa(getTextoPesquisa(), getFiltroSelecionado())
        );
    }

    // ========== MÉTODOS PRIVADOS ========== //

    private void atualizarPlaceholder() {
        programmaticChange = true;
        try {
            // Só atualiza se estiver vazio
            if (campoPesquisa.getText().isEmpty()) {
                String filtro = getFiltroSelecionado();
                campoPesquisa.setText("Pesquisar por...");
                campoPesquisa.setForeground(COR_PLACEHOLDER);
                campoPesquisa.setFont(new Font("SansSerif", Font.ITALIC, 14));

                // Remove o foco do campo de texto se estiver focado
                if (campoPesquisa.hasFocus()) {
                    campoPesquisa.transferFocusBackward(); // Ou transferFocus()
                }
            }
        } finally {
            programmaticChange = false;
        }
    }

    private boolean isPlaceholder() {
        return campoPesquisa.getForeground().equals(COR_PLACEHOLDER);
    }

    // ========== INTERFACES E CLASSES INTERNAS ========== //

    /**
     * Interface para listeners de eventos de pesquisa
     */
    public interface PesquisaListener {
        void onPesquisa(String texto, String filtro);
    }
}