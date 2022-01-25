package Utilities;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SmartJButton extends JPanel {
    public enum ImageAlignment {
        LEFT,
        RIGHT
    }
    // Components
    private JLabel icon;
    private JLabel text;

    // Component data
    private RoundedBorder border;
    private Component parent;
    private Point btnLocation = new Point(0, 0);
    protected final Color hoverColor = Color.decode("#e0e0e0"), defaultColor = Color.decode("#f0f0f0");
    public interface InteractionListener {
        void onClick();
    }

    public SmartJButton(Component parent, String text, String imageLocation, ImageAlignment alignment, InteractionListener listener) {
        this.parent = parent;

        // Set properties
        setLayout(new MigLayout("insets 10 20 10 20"));
        setLocation(btnLocation.x + 10, btnLocation.y + 10);
        setSize(parent.getHeight(), parent.getHeight());
        setBackground(parent.getBackground());
        border = new RoundedBorder();
        border.setBorderThickness(0);
        border.setBorderInsets(new Insets(0, 0, 0, 0));
        border.setBackground(defaultColor);
        setBorder(border);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Instantiate
        if (text != null) {
            this.text = new JLabel(text);
            this.text.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 16));
        }
        ImageIcon icon = new ImageIcon(imageLocation);
        icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        this.icon = new JLabel(icon);

        // Finalize
        if (text == null)
            add(this.icon);
        else
            if (alignment == ImageAlignment.LEFT) {
                add(this.icon);
                add(this.text);
            } else {
                add(this.text);
                add(this.icon);
            }

        // Setup listener
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onClick();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (border.getBackground() != hoverColor) {
                    border.setBackground(hoverColor);
                    setBorder(null);
                    setBorder(border);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (border.getBackground() != defaultColor) {
                    border.setBackground(defaultColor);
                    setBorder(null);
                    setBorder(border);
                }
            }
        });
        this.icon.addMouseListener(getMouseListeners()[0]);
        if (text != null)
            this.text.addMouseListener(getMouseListeners()[0]);
    }
}
