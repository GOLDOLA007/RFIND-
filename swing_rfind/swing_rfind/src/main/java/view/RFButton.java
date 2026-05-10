package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;

public class RFButton extends JButton {
    private int borderRadius;
    private Font quicksand;
    private Color backgroundColor;
    private Color borderColor;
    private Color fundoAlvo; // cor que será usada na transição
    private Color bordaAlvo; // borda que será usada na transição
    private Timer transitionTimer;

    public RFButton(String texto) {
        super(texto);
        try {
            InputStream fontePrincipal = RFButton.class.getResourceAsStream("/Fonts/Open_Sans/OpenSans-VariableFont_wdth,wght.ttf");
            quicksand = Font.createFont(Font.TRUETYPE_FONT, fontePrincipal);
            quicksand = quicksand.deriveFont(Font.PLAIN, 24);
        } catch (Exception e) {
            System.out.println("Exceção na fonte: " + e.getMessage());
            // Fallback para fonte padrão se a customizada não carregar
            quicksand = new Font("SansSerif", Font.PLAIN, 24);
        }

        this.borderRadius = 25;
        this.backgroundColor = Color.WHITE;
        this.borderColor = Color.WHITE;
        this.bordaAlvo = Color.WHITE;
        this.fundoAlvo = Color.WHITE;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBackground(backgroundColor);
        setForeground(Color.BLACK); // cor do texto
        setFont(quicksand);
        setBorderPainted(false); // Importante: desativa a borda padrão
        setOpaque(false); // Isso deve funcionar agora com a implementação correta do paintComponent

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                fundoAlvo = Color.decode("#BABABA");
                bordaAlvo = Color.decode("#BABABA");
                transicaoCores();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
                fundoAlvo = Color.WHITE;
                bordaAlvo = Color.WHITE;
                transicaoCores();
            }
        });
    }

    private void transicaoCores() {
        if (transitionTimer != null && transitionTimer.isRunning()) {
            transitionTimer.stop();
        }

        transitionTimer = new Timer(10, e -> {
            backgroundColor = blendColors(backgroundColor, fundoAlvo, 0.1f);
            borderColor = blendColors(borderColor, bordaAlvo, 0.1f);
            repaint();

            if (backgroundColor.equals(fundoAlvo) && borderColor.equals(bordaAlvo)) {
                ((Timer) e.getSource()).stop();
            }
        });
        transitionTimer.start();
    }

    private Color blendColors(Color start, Color target, float factor) {
        int r = (int) (start.getRed() + factor * (target.getRed() - start.getRed()));
        int g = (int) (start.getGreen() + factor * (target.getGreen() - start.getGreen()));
        int b = (int) (start.getBlue() + factor * (target.getBlue() - start.getBlue()));
        return new Color(r, g, b);
    }

    // Desenha o botão
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Ativa anti-aliasing para bordas suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Desenha o fundo arredondado
        g2d.setColor(backgroundColor);
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), borderRadius, borderRadius));

        // Desenha o texto centralizado
        g2d.setColor(getForeground());
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle stringBounds = fm.getStringBounds(this.getText(), g2d).getBounds();
        int textX = (getWidth() - stringBounds.width) / 2;
        int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();
        g2d.drawString(getText(), textX, textY);

        g2d.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // Ativa anti-aliasing para bordas suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(1.5f)); // Espessura da borda
        g2d.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, borderRadius, borderRadius));
        g2d.dispose();
    }

    // Métodos para customizar as cores
    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }

    public int getBorderRadius() {
        return borderRadius;
    }
}