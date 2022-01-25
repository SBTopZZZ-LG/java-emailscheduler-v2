package Frames;

import Utilities.Fonts;
import Utilities.GoogleAuth;
import Utilities.RoundedBorder;
import Utilities.SmartJFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class SignInFrameSignInButtonComponents {
    final JPanel container = new JPanel(new MigLayout("insets 15 15 15 15"));
    final JLabel text = new JLabel();
    final JLabel logo = new JLabel(new ImageIcon(new ImageIcon("res/images/google.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
}
class SignInFrameSignInButton extends JPanel {
    public interface OnClickListener {
        void onClick(SignInFrameSignInButton button);
    }

    final SignInFrameSignInButtonComponents components = new SignInFrameSignInButtonComponents();
    private final Dimension size = new Dimension(80, 25);
    private final RoundedBorder border = new RoundedBorder();
    protected final Color hoverColor = Color.decode("#f0f0f0"), defaultColor = Color.decode("#f7f7f7");

    public SignInFrameSignInButton(OnClickListener listener) {
        setLayout(new BorderLayout());
        setSize(size);

        components.container.setBackground(Color.WHITE);
        border.setBorderColor(Color.lightGray);
        border.setBorderThickness(0);
        border.setBackground(defaultColor);
        components.container.setBorder(border);
        add(components.container, BorderLayout.CENTER);

        components.text.setText("Sign in with ");
        components.text.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 18));
        components.container.add(components.text);
        components.container.add(components.logo);
        components.container.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onClick(SignInFrameSignInButton.this);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isEnabled())
                    return;

                if (border.getBackground() != hoverColor) {
                    border.setBackground(hoverColor);
                    components.container.setBorder(null);
                    components.container.setBorder(border);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isEnabled())
                    return;

                if (border.getBackground() != defaultColor) {
                    border.setBackground(defaultColor);
                    components.container.setBorder(null);
                    components.container.setBorder(border);
                }
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            components.text.setForeground(Color.LIGHT_GRAY);
        } else {
            components.text.setForeground(Color.BLACK);
        }

        if (border.getBackground() != defaultColor) {
            border.setBackground(defaultColor);
            components.container.setBorder(null);
            components.container.setBorder(border);
        }

        super.setEnabled(enabled);
    }
}
class SignInFrameComponents {
    final JPanel container = new JPanel(new MigLayout("fill, insets 35 35 35 35"));
    final JLabel heading = new JLabel();
    final JTextArea body = new JTextArea();
    SignInFrameSignInButton button;
    final JLabel errorText = new JLabel("Sign in failed. Please check your internet connection and try again.");
}
public class SignInFrame extends SmartJFrame {
    final SignInFrameComponents components = new SignInFrameComponents();
    private final Dimension size = new Dimension(600, 300);
    public final String title = "Email Scheduler - Sign in to Google";

    public SignInFrame() {
        super(null);

        setTitle(title);
        setLayout(new BorderLayout());
        setSize(size);
        setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - size.width / 2, screenSize.height / 2 - size.height / 2);

        components.container.setBackground(Color.WHITE);
        add(components.container, BorderLayout.CENTER);

        components.heading.setText("Sign in needed");
        components.heading.setFont(Fonts.getBold().deriveFont(Font.PLAIN, 30));
        components.heading.setHorizontalAlignment(JLabel.CENTER);
        components.container.add(components.heading, "width 100%, wrap");

        components.body.setEditable(false);
        components.body.setLineWrap(true);
        components.body.setText("We need access to your Google account in order to process emails,\nwithout which the application cannot proceed.");
        components.body.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 17));
        components.container.add(components.body, "width 100%, height 100%, wrap");

        components.button = new SignInFrameSignInButton(new SignInFrameSignInButton.OnClickListener() {
            @Override
            public void onClick(SignInFrameSignInButton button) {
                components.errorText.setVisible(false);
                button.setEnabled(false);

                GoogleAuth.init(new GoogleAuth.Listener() {
                    @Override
                    public void onSuccess() {
                        button.setEnabled(true);

                        MainFrame mainFrame = new MainFrame(SignInFrame.this);
                        pushNext(mainFrame, false);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        button.setEnabled(true);

                        components.errorText.setVisible(true);
                    }
                });
            }
        });
        components.container.add(components.button, "align center, wrap");

        components.errorText.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 19));
        components.errorText.setForeground(Color.decode("#cc0000"));
        components.errorText.setVisible(false);
        components.container.add(components.errorText);
    }
}
