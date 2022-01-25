package Frames;

import Models.Entry;
import Models.EntryHandler;
import Models.EntryTimer;
import Utilities.Fonts;
import Utilities.GoogleAuth;
import Utilities.RoundedBorder;
import Utilities.SmartJFrame;
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
    protected final Color hoverColor = Color.decode("#f0f0f0"), defaultColor = Color.decode("#f7f7f7");

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

        ImageIcon icon = new ImageIcon("res/images/plus.png");
        icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        components.title = new JLabel(icon);

        parent.add(components.title, "width 100%, height 100%");

        parent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        parent.setToolTipText("Create a New Entry");
    }
}

class MainFrameEntryCardControls extends JPanel {
    final RoundedBorder border = new RoundedBorder();
    final JLabel close = new JLabel(new ImageIcon(new ImageIcon("res/images/cancel.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
    final JLabel edit = new JLabel(new ImageIcon(new ImageIcon("res/images/pencil.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
    final boolean isPending;
    final JLabel pending = new JLabel();

    public MainFrameEntryCardControls(boolean isPending) {
        this.isPending = isPending;
        setLayout(new MigLayout("fillx, insets 5 10 5 10"));

        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.setToolTipText("Cancel entry");
        edit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        edit.setToolTipText("Edit entry");
        pending.setText(isPending ? "Pending" : "Sent");
        pending.setHorizontalAlignment(JLabel.CENTER);
        pending.setForeground(Color.WHITE);
        pending.setFont(Fonts.getBold().deriveFont(Font.PLAIN, 20));
        add(close);
        add(pending, "width 100%");
        if (isPending)
            add(edit);
    }
}
class SmartJPanel extends JPanel {
    public SmartJPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    public void paint(Graphics g) {
        paintChildren(g);
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
    private Entry entry;

    public MainFrameEntryCard(final Entry entry) {
        super(true);

        this.entry = entry;

        initComponents(this);
    }

    @Override
    protected void onMouseEvent(boolean inside) {
        if (inside) {
            components.body.setText("<html><p style='background: #f0f0f0'>“ " + entry.getBody() + " ”<p></html>");
        } else {
            components.body.setText("<html><p style='background: #f7f7f7'>“ " + entry.getBody() + " ”<p></html>");
        }
    }

    @Override
    public void initComponents(JPanel parent) {
        components = new MainFrameEntryCardComponents();
        components.cardControls = new MainFrameEntryCardControls(entry.isPending());

        components.target.setText("To: " + entry.getRecipientEmail());
        components.target.setFont(Fonts.getBold().deriveFont(Font.BOLD, 18));
        components.subject.setText("Subject: " + entry.getSubject());
        components.subject.setFont(Fonts.getLight().deriveFont(Font.BOLD, 16));
        components.body.setText("<html><p style='background: #f7f7f7'>“ " + entry.getBody() + " ”<p></html>");
        components.body.setToolTipText(entry.getBody());
        components.body.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 16));
        components.body.setBackground(Color.WHITE);
        components.body.addMouseListener(getMouseListeners()[0]);
        components.cardControls.close.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
        parent.add(components.bodyContainer, "width 100%, height 100%, wrap");
        parent.add(components.cardControls, "width 100%");
    }
}

class MainFrameLogOutButtonComponents {
    final JPanel container = new JPanel(new MigLayout("insets 15 15 15 15"));
    final JLabel text = new JLabel();
    final JLabel logo = new JLabel(new ImageIcon(new ImageIcon("res/images/logout.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
}
class MainFrameLogOutButton extends JPanel {
    public interface OnClickListener {
        void onClick(MainFrameLogOutButton button);
    }

    final MainFrameLogOutButtonComponents components = new MainFrameLogOutButtonComponents();
    private final Dimension size = new Dimension(80, 25);
    private final RoundedBorder border = new RoundedBorder();
    protected final Color hoverColor = Color.decode("#f0f0f0"), defaultColor = Color.decode("#f7f7f7");

    public MainFrameLogOutButton(OnClickListener listener) {
        setLayout(new BorderLayout());
        setSize(size);

        components.container.setBackground(Color.WHITE);
        border.setBorderColor(Color.lightGray);
        border.setBorderThickness(0);
        border.setBackground(defaultColor);
        components.container.setBorder(border);
        add(components.container, BorderLayout.CENTER);

        components.text.setText("Log out ");
        components.text.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 18));
        components.container.add(components.text);
        components.container.add(components.logo);
        components.container.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onClick(MainFrameLogOutButton.this);
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
class MainFrameComponents {
    final JPanel container = new JPanel(new MigLayout("fill"));
    final JPanel cards = new JPanel();
    final JLabel title = new JLabel("Email Scheduler");
    MainFrameLogOutButton button;
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
        setLayout(new MigLayout("fill, insets 0 0 0 0"));
        setSize(600, 500);

        components.title.setFont(Fonts.getBold().deriveFont(Font.BOLD, 35));
        components.button = new MainFrameLogOutButton(new MainFrameLogOutButton.OnClickListener() {
            @Override
            public void onClick(MainFrameLogOutButton button) {
                try {
                    GoogleAuth.signOut();
                } catch (Exception e) {}
                pop();
            }
        });
        components.container.add(components.title);
        components.container.add(components.button, "align right, wrap");
        components.cards.setLayout(new MigLayout("fill"));

        components.container.add(components.scrollPane, "spanx, gapy 20px");
        loadEntries();
        EntryHandler.callbacks.add(new EntryHandler.Callback() {
            @Override
            public void updated() {
                loadEntries(false);
            }
        });
        components.cards.setBackground(Color.WHITE);
        components.scrollPane.setBorder(null);
        components.container.setBackground(Color.WHITE);
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
            MainFrameEntryCard entryCard = new MainFrameEntryCard(entryTimer.entry);
            entryCard.components.cardControls.edit.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    EditEntry editEntry = new EditEntry(MainFrame.this, entryTimer.entry.id);
                    pushNext(editEntry, true);
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
