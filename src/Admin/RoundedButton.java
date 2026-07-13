package Admin;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    private Color normalColor;
    private Color activeColor = new Color(30, 136, 229);
    private boolean active = false;

    public RoundedButton(String text, Color color) {
        super(text);
        this.normalColor = color;

        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(color);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void setActive(boolean active) {
        this.active = active;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(active ? activeColor : normalColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(g2);
        g2.dispose();
    }
}
