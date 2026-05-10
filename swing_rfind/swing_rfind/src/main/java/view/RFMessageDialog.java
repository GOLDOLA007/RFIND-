package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RFMessageDialog extends JDialog {
    private RFLabel messageLabel;
    private Color verydarkgray;
    private final int MAX_WIDTH = 600; // Largura máxima permitida
    private final int MIN_WIDTH = 300; // Largura mínima
    private final int MIN_HEIGHT = 150; // Altura mínima
    private final int PADDING = 30; // Espaçamento interno

    public RFMessageDialog(Frame parent, String message, String title) {
        super(parent, title, true);
        verydarkgray = new Color(32, 35, 42);

        // Configuração básica
        setLayout(new BorderLayout());
        getContentPane().setBackground(verydarkgray);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false); // Mantém não redimensionável para evitar problemas de layout

        // Cria o label da mensagem com quebra de linha automática
        messageLabel = new RFLabel(message);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Painel de rolagem para mensagens longas
        JScrollPane scrollPane = new JScrollPane(messageLabel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(verydarkgray);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        // Botão OK
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        RFButton okButton = new RFButton("OK");
        okButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Calcula o tamanho ideal
        calculateAndSetSize(message);

        // Centraliza na tela
        setLocationRelativeTo(parent);

        // Listener para redimensionar se necessário (caso o texto mude dinamicamente)
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                calculateAndSetSize(message);
            }
        });
    }

    private void calculateAndSetSize(String message) {
        // Cria um label temporário para calcular as dimensões
        JLabel tempLabel = new JLabel(message);
        tempLabel.setFont(messageLabel.getFont());
        tempLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Calcula a largura preferida do texto
        int textWidth = tempLabel.getPreferredSize().width;

        // Define a largura do diálogo (limitando ao máximo permitido)
        int dialogWidth = Math.min(textWidth + PADDING * 2, MAX_WIDTH);
        dialogWidth = Math.max(dialogWidth, MIN_WIDTH); // Garante a largura mínima

        // Força o label a ter a largura calculada para determinar a altura
        tempLabel.setSize(new Dimension(dialogWidth - PADDING * 2, Integer.MAX_VALUE));
        int textHeight = tempLabel.getPreferredSize().height;

        // Define a altura do diálogo (com espaço para o botão)
        int dialogHeight = Math.max(textHeight + PADDING * 2 + 60, MIN_HEIGHT);

        // Aplica o tamanho calculado
        setSize(dialogWidth, dialogHeight);

    }

    public static void showMessageDialog(Component parent, String message, String title) {
        SwingUtilities.invokeLater(() -> {
            Frame parentFrame = JOptionPane.getFrameForComponent(parent);
            RFMessageDialog dialog = new RFMessageDialog(parentFrame, message, title);
            dialog.setVisible(true);
        });
    }
}