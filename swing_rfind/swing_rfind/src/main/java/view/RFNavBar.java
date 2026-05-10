package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RFNavBar extends JPanel {

    //Variável para mapear qual botão foi pressioando
    private Map<String, JButton> botoes = new HashMap<>();

    //Construtor
    public RFNavBar(Font fonte, Color fundo) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        setBackground(fundo);
        setOpaque(false);

        String[] navItems = {"Funcionário","Produto","Local","Registros"};
        for (String item : navItems) {
            JButton btn = new JButton(item);
            btn.setForeground(Color.WHITE);
            btn.setBackground(fundo);
            btn.setFont(fonte.deriveFont(Font.PLAIN, 23));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            add(btn);
            botoes.put(item, btn);
        }
    }

    public JButton getBotao(String nome) {
        return botoes.get(nome);
    }
}

