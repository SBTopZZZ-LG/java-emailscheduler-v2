package Utilities;

import javax.swing.*;
import java.awt.*;

public class SmartJPanel extends JPanel {
    public SmartJPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    public void paint(Graphics g) {
        paintChildren(g);
    }
}
