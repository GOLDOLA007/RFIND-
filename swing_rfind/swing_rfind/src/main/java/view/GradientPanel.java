package view;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private Color color1 = Color.BLACK;
    private Color color2 = new Color(0x2c2d38);

    public GradientPanel() {
        setOpaque(false); // Importante: este painel é transparente
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();

        GradientPaint gp = new GradientPaint(
                0, 0, color1,
                0, height, color2
        );

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }

    // Métodos para customizar as cores do gradiente
    public void setGradientColors(Color startColor, Color endColor) {
        this.color1 = startColor;
        this.color2 = endColor;
        repaint();
    }

    public Color[] getGradientColors() {
        return new Color[]{color1, color2};
    }
}