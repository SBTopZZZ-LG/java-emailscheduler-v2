package Frames;

import Models.Entry;
import Utilities.EntryHandler;
import Models.EntryTimer;
import Utilities.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

abstract class MainFrameCard extends JPanel {
    private final RoundedBorder border = new RoundedBorder();
    protected final Color hoverColor = Color.decode("#e0e0e0"), defaultColor = Color.decode("#f0f0f0");

    protected void onMouseEvent(boolean inside) {}

    private void init() {
        final int size = 300;
        setLayout(new MigLayout("width " + size + "px, height " + size + "px, insets 0 0 0 0"));
        setBackground(Color.WHITE);
        border.setBorderThickness(0);
        border.setBackground(defaultColor);
        border.setSecondaryBackground(Color.WHITE);
        border.setBorderColor(Color.darkGray);
        border.setBorderInsets(new Insets(1, 1, 1, 1));
        setBorder(border);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

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

                    onMouseEvent(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (border.getBackground() != defaultColor) {
                    border.setBackground(defaultColor);
                    setBorder(null);
                    setBorder(border);

                    onMouseEvent(false);
                }
            }
        });
    }

    public MainFrameCard() {
        init();

        initComponents(this);
    }
    public MainFrameCard(boolean skipInit) {
        init();

        if (!skipInit)
            initComponents(this);
    }

    public abstract void initComponents(JPanel parent);
}

class MainFrameNewCardComponents {
    JLabel title;
}
class MainFrameNewCard extends MainFrameCard {
    private MainFrameNewCardComponents components;

    @Override
    public void initComponents(JPanel parent) {
        components = new MainFrameNewCardComponents();

        // Initialize
        ImageIcon icon = new ImageIcon("res/images/plus.png");
        icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        components.title = new JLabel(icon);

        // Finalize
        parent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        parent.setToolTipText("Create a New Entry");
        parent.add(components.title, "width 100%, height 100%");
    }
}

class MainFrameEntryCardControls extends JPanel {
    public interface InteractionListener {
        void onClose();
        void onEdit();
    }

    protected final RoundedBorder border = new RoundedBorder();

    protected final JLabel close = new JLabel(new ImageIcon(new ImageIcon("res/images/cancel.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH))) {{
        setVisible(false);
    }};
    protected final JLabel edit = new JLabel(new ImageIcon(new ImageIcon("res/images/pencil.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH))) {{
        setVisible(false);
    }};
    protected final JLabel pending = new JLabel();

    protected final boolean isPending;

    public MainFrameEntryCardControls(boolean isPending, InteractionListener listener) {
        this.isPending = isPending;

        // Paint
        setLayout(new MigLayout("fillx, insets 5 10 5 10"));

        // Instantiate
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.setToolTipText("Cancel entry");
        close.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onClose();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        edit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        edit.setToolTipText("Edit entry");
        edit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onEdit();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        pending.setText(isPending ? "Pending" : "Sent");
        pending.setHorizontalAlignment(JLabel.CENTER);
        pending.setForeground(Color.WHITE);
        pending.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 20));

        // Finalize
        add(close);
        add(pending, "width 100%");
        if (isPending)
            add(edit);
    }
}
class MainFrameEntryCardComponents {
    final SmartJPanel bodyContainer = new SmartJPanel(new MigLayout());
    final JLabel target = new JLabel();
    final JLabel subject = new JLabel();
    final JLabel body = new JLabel();
    MainFrameEntryCardControls cardControls;
}
class MainFrameEntryCard extends MainFrameCard {
    MainFrameEntryCardComponents components;

    private final Entry entry;
    private boolean controlsVisible = false;
    public final MainFrame parent;

    public MainFrameEntryCard(MainFrame parent, Entry entry) {
        super(true);

        this.entry = entry;
        this.parent = parent;

        initComponents(this);
    }

    @Override
    protected void onMouseEvent(boolean inside) {
        if (inside) {
            components.body.setText("<html><p style='background: #e0e0e0'>“ " + entry.getBody() + " ”<p></html>");
        } else {
            components.body.setText("<html><p style='background: #f0f0f0'>“ " + entry.getBody() + " ”<p></html>");
        }
    }

    @Override
    public void initComponents(JPanel parent2) {
        components = new MainFrameEntryCardComponents();

        // Initialize
        components.cardControls = new MainFrameEntryCardControls(entry.isPending(), new MainFrameEntryCardControls.InteractionListener() {
            @Override
            public void onClose() {
                if (!components.cardControls.close.isVisible())
                    return;

                EntryTimer entryTimer = null;
                for (EntryTimer _entryTimer : EntryHandler.entries)
                    if (_entryTimer.entry == entry) {
                        entryTimer = _entryTimer;
                        break;
                    }

                if (entryTimer == null)
                    return;

                // Check if timer is enabled, if yes then cancel first
                if (entryTimer.timer != null) {
                    entryTimer.timer.cancel();
                    entryTimer.timer = null;
                }
                EntryHandler.entries.remove(entryTimer);

                // Save and Refresh
                try {
                    EntryHandler.save();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                EntryHandler.notifyChange();
            }

            @Override
            public void onEdit() {
                if (!components.cardControls.edit.isVisible())
                    return;

                EditEntry editEntry = new EditEntry(parent, entry.id);
                parent.pushNext(editEntry, true);
            }
        });
        components.cardControls.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!controlsVisible) {
                    components.cardControls.close.setVisible(true);
                    components.cardControls.edit.setVisible(true);
                    controlsVisible = true;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (controlsVisible) {
                    components.cardControls.close.setVisible(false);
                    components.cardControls.edit.setVisible(false);
                    controlsVisible = false;
                }
            }
        });
        components.cardControls.edit.addMouseListener(components.cardControls.getMouseListeners()[0]);

        components.target.setText("To: " + entry.getRecipientEmail());
        components.target.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 18));

        components.subject.setText("Subject: " + entry.getSubject());
        components.subject.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 16));

        components.body.setText("<html><p style='background: #f0f0f0' font-family: Arial>“ " + entry.getBody() + " ”<p></html>");
        components.body.setToolTipText(entry.getBody());
        components.body.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 16));
        components.body.setBackground(Color.WHITE);
        components.body.addMouseListener(getMouseListeners()[0]);

        components.cardControls.border.setBorderColor(Color.lightGray);
        components.cardControls.border.setBorderThickness(0.5f);
        components.cardControls.border.getCorners().clear().bottom();
        components.cardControls.border.setBorderInsets(new Insets(0, 0, 0, 0));
        components.cardControls.border.setBackground(Color.gray);

        components.cardControls.setBackground(Color.WHITE);
        components.cardControls.setBorder(components.cardControls.border);

        components.bodyContainer.add(components.target, "wrap");
        components.bodyContainer.add(components.subject, "wrap");
        components.bodyContainer.add(components.body, "width 100%, height 100%, wrap");

        // Finalize
        parent2.add(components.bodyContainer, "width 100%, height 100%, wrap");
        parent2.add(components.cardControls, "width 100%");
    }
}

class MainFrameComponents {
    final JPanel container = new JPanel(new MigLayout("fill"));
    final JPanel cards = new JPanel(new MigLayout("fill"));
    final JLabel title = new JLabel("Email Scheduler");
    SmartJButton logOutButton;
    SmartJButton sourceCodeButton;
    final JScrollPane scrollPane = new JScrollPane(cards, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    final List<MainFrameCard> cardObjects = new ArrayList<>();
}
public class MainFrame extends SmartJFrame {
    MainFrameComponents components;
    public final String title = "Email Scheduler - Dashboard";

    public MainFrame(SmartJFrame previous) {
        super(previous);
        components = new MainFrameComponents();

        // Init entry
        EntryHandler.registerTimers(null);

        // Paint
        setTitle(title);
        setBackground(Color.WHITE);
        setLayout(new MigLayout("fill, insets 0 0 0 0"));
        setSize(600, 500);

        components.container.setBackground(Color.WHITE);

        components.cards.setBackground(Color.WHITE);

        components.title.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 35));
        components.container.add(components.title, "width 100%");
        components.logOutButton = new SmartJButton(MainFrame.this, "Log Out", "res/images/logout.png", SmartJButton.ImageAlignment.RIGHT,
                new SmartJButton.InteractionListener() {
            @Override
            public void onClick() {
                try {
                    GoogleAuth.signOut();
                } catch (Exception e) {}
                pop();
            }
        });
        components.sourceCodeButton = new SmartJButton(MainFrame.this, null, "res/images/github.png", SmartJButton.ImageAlignment.RIGHT,
                new SmartJButton.InteractionListener() {
            @Override
            public void onClick() {
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://github.com/SBTopZZZ-LG/java-emailscheduler-v2.git"));
                } catch (java.io.IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        components.container.add(components.sourceCodeButton);
        components.container.add(components.logOutButton, "wrap");

        components.scrollPane.setBorder(null);
        components.container.add(components.scrollPane, "grow, span 3, gapy 20px");

        loadEntries();
        EntryHandler.callbacks.add(new EntryHandler.Callback() {
            @Override
            public void updated() {
                loadEntries(false);
            }
        });

        // Finalize
        add(components.container, "width 100%, height 100%");
    }

    private void loadEntries() {
        loadEntries(true);
    }
    private void loadEntries(boolean skipRefresh) {
        components.cardObjects.clear();
        components.cards.removeAll();

        MainFrameNewCard newCard = new MainFrameNewCard();
        newCard.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NewEntry newEntry = new NewEntry(MainFrame.this);
                pushNext(newEntry, true);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        components.cardObjects.add(newCard);
        for (EntryTimer entryTimer : EntryHandler.entries) {
            MainFrameEntryCard entryCard = new MainFrameEntryCard(MainFrame.this, entryTimer.entry);
            components.cardObjects.add(entryCard);
        }
        for (MainFrameCard card : components.cardObjects)
            components.cards.add(card);

        if (!skipRefresh) {
            components.cards.revalidate();
            components.cards.repaint();
        }
    }

    @Override
    public boolean onClose(WindowEvent windowEvent) {
        EntryHandler.unregisterTimers();
        return super.onClose(windowEvent);
    }
}
