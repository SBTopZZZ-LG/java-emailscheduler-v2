package Utilities;

import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoundedBorder implements Border {
    public static class Corners {
        private List<String> selected;

        public Corners() {
            selected = new ArrayList<>();
        }

        public Corners clear() {
            selected.clear();
            return this;
        }

        public Corners all() {
            top();
            return bottom();
        }

        public Corners top() {
            if (!selected.contains("topLeft"))
                selected.add("topLeft");
            if (!selected.contains("topRight"))
                selected.add("topRight");
            return this;
        }
        public Corners bottom() {
            if (!selected.contains("bottomLeft"))
                selected.add("bottomLeft");
            if (!selected.contains("bottomRight"))
                selected.add("bottomRight");
            return this;
        }
        public Corners left() {
            if (!selected.contains("topLeft"))
                selected.add("topLeft");
            if (!selected.contains("bottomLeft"))
                selected.add("bottomLeft");
            return this;
        }
        public Corners right() {
            if (!selected.contains("topRight"))
                selected.add("topRight");
            if (!selected.contains("bottomRight"))
                selected.add("bottomRight");
            return this;
        }

        public Corners topLeft() {
            if (!selected.contains("topLeft"))
                selected.add("topLeft");
            return this;
        }
        public Corners topRight() {
            if (!selected.contains("topLeft"))
                selected.add("topLeft");
            return this;
        }
        public Corners bottomLeft() {
            if (!selected.contains("topLeft"))
                selected.add("topLeft");
            return this;
        }
        public Corners bottomRight() {
            if (!selected.contains("topLeft"))
                selected.add("topLeft");
            return this;
        }

        public boolean isTopLeft() {
            return selected.contains("topLeft");
        }
        public boolean isBottomLeft() {
            return selected.contains("bottomLeft");
        }
        public boolean isTopRight() {
            return selected.contains("topRight");
        }
        public boolean isBottomRight() {
            return selected.contains("bottomRight");
        }
    }
    private Insets borderInsets = new Insets(5, 5, 5, 5);
    private int cornerRadius = 20;
    private float borderThickness = 1f;
    private Color borderColor = Color.BLACK, background, secondaryBackground;
    private Corners corners;

    public RoundedBorder() {
        corners = new Corners().all();
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;

        // If secondaryBackground is not null, apply it before continuing
        if (secondaryBackground != null) {
            g2.setColor(secondaryBackground);

            // Paint full
            g2.setBackground(secondaryBackground);
        }

        // Set properties to g2 before painting
        g2.setColor(borderColor);
        if (borderThickness > 0)
            g2.setStroke(new BasicStroke(borderThickness));

        // Draw lines if borderThickness > 0
        if (borderThickness > 0) {
            g2.drawLine(x + (borderInsets.left + cornerRadius), y + borderInsets.top,
                    x + width - (borderInsets.right + cornerRadius), y + borderInsets.top); // Top
            g2.drawLine(x + (borderInsets.left + cornerRadius), y + height - borderInsets.bottom,
                    x + width - (borderInsets.right + cornerRadius), y + height - borderInsets.bottom); // Bottom
            g2.drawLine(x + borderInsets.left, y + (borderInsets.top + cornerRadius),
                    x + borderInsets.left, y + height - (borderInsets.bottom + cornerRadius)); // Left
            g2.drawLine(x + width - borderInsets.right, y + (borderInsets.top + cornerRadius),
                    x + width - borderInsets.right, y + height - (borderInsets.bottom + cornerRadius)); // Right
        }

        // Draw arcs if borderThickness > 0
        if (borderThickness > 0) {
            if (corners.isTopLeft())
                // Top Left Curved
                g2.drawArc(x + borderInsets.left, y + borderInsets.top,
                    cornerRadius * 2, cornerRadius * 2,
                    90, 90);
            else {
                // Top Left Boxed
                g2.drawLine(x + borderInsets.left, y + borderInsets.top, x + borderInsets.left + cornerRadius, y + borderInsets.top);
                g2.drawLine(x + borderInsets.left, y + borderInsets.top + cornerRadius, x + borderInsets.left, y + borderInsets.top);
            }
            if (corners.isTopRight())
                // Top Right Curved
                g2.drawArc(x + width - (borderInsets.right + cornerRadius * 2), y + borderInsets.top,
                    cornerRadius * 2, cornerRadius * 2,
                    0, 90);
            else {
                // Top Right Boxed
                g2.drawLine(x + width - (borderInsets.right + cornerRadius), y + borderInsets.top, x + width - borderInsets.right, y + borderInsets.top);
                g2.drawLine(x + width - borderInsets.right, y + borderInsets.top, x + width - borderInsets.right, borderInsets.top + cornerRadius);
            }
            if (corners.isBottomLeft())
                // Bottom Left Curved
                g2.drawArc(x + borderInsets.left, y + height - (borderInsets.bottom + cornerRadius * 2),
                    cornerRadius * 2, cornerRadius * 2,
                    180, 90);
            else {
                // Bottom Left Boxed
                g2.drawLine(x + borderInsets.left, y + height - (borderInsets.bottom), x + borderInsets.left + cornerRadius, y + height - (borderInsets.bottom));
                g2.drawLine(x + borderInsets.left, y + height - (borderInsets.bottom + cornerRadius), x + borderInsets.left, y + height - (borderInsets.bottom));
            }
            if (corners.isBottomRight())
                // Bottom Right Curved
                g2.drawArc(x + width - (borderInsets.right + cornerRadius * 2), y + height - (borderInsets.bottom + cornerRadius * 2),
                    cornerRadius * 2, cornerRadius * 2,
                    270, 90);
            else {
                // Bottom Right Boxed
                g2.drawLine(x + width - (borderInsets.right + cornerRadius), y + height - borderInsets.bottom, x + width - borderInsets.right, y + height - borderInsets.bottom);
                g2.drawLine(x + width - borderInsets.right, y + height - borderInsets.bottom, x + width - borderInsets.right, y + height - (borderInsets.bottom + cornerRadius));
            }
        }

        // Paint background (if specified)
        if (background != null) {
            g2.setColor(background);
            g2.fillRoundRect(x + borderInsets.left, y + borderInsets.top,
                    width - (borderInsets.left + borderInsets.right), height - (borderInsets.top + borderInsets.bottom),
                    cornerRadius * 2, cornerRadius * 2);

            // Fill corners with squares if no curves
            if (!corners.isTopLeft())
                g2.fillRect(x + borderInsets.left, y + borderInsets.top, cornerRadius, cornerRadius);
            if (!corners.isTopRight())
                g2.fillRect(x + width - (borderInsets.right + cornerRadius), borderInsets.top, cornerRadius, cornerRadius);
            if (!corners.isBottomLeft())
                g2.fillRect(x + borderInsets.left, y + height - (borderInsets.bottom + cornerRadius), cornerRadius, cornerRadius);
            if (!corners.isBottomRight())
                g2.fillRect(x + width - (borderInsets.right + cornerRadius), x + height - (borderInsets.bottom + cornerRadius), cornerRadius, cornerRadius);
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return borderInsets;
    }
    public void setBorderInsets(Insets borderInsets) {
        this.borderInsets = borderInsets;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public float getBorderThickness() {
        return this.borderThickness;
    }
    public void setBorderThickness(float borderThickness) {
        this.borderThickness = borderThickness;
    }

    public Color getBorderColor() { return this.borderColor; }
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getBackground() {
        return this.background;
    }
    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getSecondaryBackground() {
        return this.secondaryBackground;
    }
    public void setSecondaryBackground(Color secondaryBackground) {
        this.secondaryBackground = secondaryBackground;
    }

    public Corners getCorners() {
        return corners;
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
