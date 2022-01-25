package Utilities;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HeadButton extends JPanel {
    // Components
    private JLabel icon;
    private JLabel text;

    // Component data
    private RoundedBorder border;
    private Component parent;
    private Point btnLocation = new Point(0, 0);
    protected final Color hoverColor = Color.decode("#f0f0f0"), defaultColor = Color.decode("#f7f7f7");
    public interface InteractionListener {
        void onClick();
    }

    public HeadButton(Component parent, String text, String imageLocation, InteractionListener listener) {
        this.parent = parent;

        // Set properties
        setLayout(new MigLayout("insets 10 20 10 20"));
        setLocation(btnLocation.x + 10, btnLocation.y + 10);
        setSize(parent.getHeight(), parent.getHeight());
        setBackground(parent.getBackground());
        border = new RoundedBorder();
        border.setBorderThickness(0);
        border.setBackground(defaultColor);
        setBorder(border);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Instantiate
        this.text = new JLabel(text);
        this.text.setFont(Fonts.getRegular().deriveFont(Font.BOLD, 16));
        ImageIcon icon = new ImageIcon(imageLocation);
        icon = new ImageIcon(icon.getImage().getScaledInstance((int) (getWidth() / 2), (int) (getHeight() / 2), Image.SCALE_SMOOTH));
        this.icon = new JLabel(icon);

        // Finalize
        add(this.icon);
        add(this.text);

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
        this.text.addMouseListener(getMouseListeners()[0]);
    }
}
