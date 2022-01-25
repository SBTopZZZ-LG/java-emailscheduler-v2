package Frames;

import Utilities.Fonts;
import Utilities.GoogleAuth;
import Utilities.SmartJButton;
import Utilities.SmartJFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class SignInFrameComponents {
    final JPanel container = new JPanel(new MigLayout("fill, insets 35 35 35 35"));
    final JLabel heading = new JLabel();
    final JTextArea body = new JTextArea();
    SmartJButton signIn;
    final JLabel errorText = new JLabel("Sign in failed. Please check your internet connection and try again.");
}
public class SignInFrame extends SmartJFrame {
    private final SignInFrameComponents components = new SignInFrameComponents();

    private final Dimension size = new Dimension(600, 300);
    public final String title = "Email Scheduler - Sign in to Google";

    public SignInFrame() {
        super(null);

        // Paint
        setTitle(title);
        setLayout(new BorderLayout());
        setSize(size);
        setResizable(false);
        setBackground(Color.WHITE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - size.width / 2, screenSize.height / 2 - size.height / 2);

        // Container
        components.container.setBackground(Color.WHITE);
        add(components.container, BorderLayout.CENTER);

        // Components
        components.heading.setText("Sign in needed");
        components.heading.setFont(Fonts.getBold().deriveFont(Font.PLAIN, 30));
        components.heading.setHorizontalAlignment(JLabel.CENTER);
        components.container.add(components.heading, "width 100%, wrap");

        components.body.setEditable(false);
        components.body.setLineWrap(true);
        components.body.setText("We need access to your Google account in order to process emails,\nwithout which the application cannot proceed.");
        components.body.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 17));
        components.container.add(components.body, "width 100%, height 100%, wrap");

        components.signIn = new SmartJButton(SignInFrame.this, "Sign in with", "res/images/google.png", SmartJButton.ImageAlignment.RIGHT,
                new SmartJButton.InteractionListener() {
                    @Override
                    public void onClick() {
                        components.errorText.setVisible(false);
                        components.signIn.setEnabled(false);

                        GoogleAuth.init(new GoogleAuth.Listener() {
                            @Override
                            public void onSuccess() {
                                components.signIn.setEnabled(true);

                                MainFrame mainFrame = new MainFrame(SignInFrame.this);
                                pushNext(mainFrame, false);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                components.signIn.setEnabled(true);

                                components.errorText.setVisible(true);
                            }
                        });
                    }
                });
        components.container.add(components.signIn, "align center, wrap");

        components.errorText.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 19));
        components.errorText.setForeground(Color.decode("#cc0000"));
        components.errorText.setVisible(false);
        components.container.add(components.errorText);
    }
}
