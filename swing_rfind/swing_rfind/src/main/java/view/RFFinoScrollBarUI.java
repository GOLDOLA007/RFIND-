package view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class RFFinoScrollBarUI extends BasicScrollBarUI {
    private Color thumbColor = new Color(100, 100, 100);
    private Color trackColor = new Color(32, 35, 42);

    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = thumbColor;
        this.trackColor = trackColor;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(thumbColor);
        g2.fillRoundRect(
                thumbBounds.x + 2,
                thumbBounds.y + 2,
                thumbBounds.width - 4,
                thumbBounds.height - 4,
                4, 4
        );

        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(trackColor);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(5, 5); // Largura fina da scrollbar
    }
}