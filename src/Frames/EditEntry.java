package Frames;

import Models.Entry;
import Utilities.EntryHandler;
import Models.EntryTimer;
import Utilities.Fonts;
import Utilities.SmartJButton;
import Utilities.SmartJFrame;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import net.miginfocom.swing.MigLayout;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

class EditEntryHeadComponents {
    SmartJButton backButton;
    SmartJButton saveButton;
}
class EditEntryHead extends JPanel {
    public interface InteractionListener {
        void onBack(EditEntryHead head);
        void onSave(EditEntryHead head);
    }

    private EditEntry parent;
    private final EditEntryHeadComponents components = new EditEntryHeadComponents();

    private int headSize = 50;
    private Point headLocation = new Point(0, 0);

    public EditEntryHead(EditEntry parent, InteractionListener listener) {
        this.parent = parent;

        // Paint
        setSize(parent.getSize().width, headSize);
        setLocation(headLocation.x, headLocation.y);
        setBackground(Color.WHITE);
        setLayout(new MigLayout("insets 5 10 5 10, fillx"));

        // Instantiate
        components.backButton = new SmartJButton(this, "Go Back", "res/images/back.png", SmartJButton.ImageAlignment.LEFT,
                new SmartJButton.InteractionListener() {
                    @Override
                    public void onClick() {
                        listener.onBack(EditEntryHead.this);
                    }
                });
        components.saveButton = new SmartJButton(this, "Save entry", "res/images/save.png", SmartJButton.ImageAlignment.LEFT,
                new SmartJButton.InteractionListener() {
                    @Override
                    public void onClick() {
                        listener.onSave(EditEntryHead.this);
                    }
                });

        // Finalize
        add(components.backButton, "gapy 10 0, width 80%");
        add(components.saveButton, "gapy 10 0, width 80%");
    }
}

class EditEntryBodyComponents {
    final JPanel recipientEmailPanel = new JPanel(new MigLayout("fillx, insets 0 0 0 0"));
    final JPanel subjectPanel = new JPanel(new MigLayout("fillx, insets 0 0 0 0"));
    final JPanel bodyPanel = new JPanel(new MigLayout("fillx, insets 0 0 0 0"));
    final JPanel schedulePanel = new JPanel(new MigLayout("fillx, insets 0 0 0 0"));

    final JLabel recipientEmail = new JLabel("Recipient's Email >");
    final JLabel subject = new JLabel("Subject >");
    final JLabel body = new JLabel("Body of Email >");
    final JLabel schedule = new JLabel("Schedule at >");

    final JTextField recipientEmailTF = new JTextField();
    final JTextField subjectTF = new JTextField();
    final JTextArea bodyTA = new JTextArea();

    final JDatePanelImpl scheduleDate = new JDatePanelImpl(new UtilDateModel(), new Properties() {{
        put("text.today", "Today");
        put("text.month", "Month");
        put("text.year", "Year");
    }});
    final TimePicker timePicker = new TimePicker(new TimePickerSettings() {{
        setColor(TimePickerSettings.TimeArea.TimePickerTextValidTime, Color.black);
        initialTime = LocalTime.now();
    }});

    final JScrollPane bodyScroll = new JScrollPane(bodyTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
}
class EditEntryBody extends JPanel {
    private EditEntry parent;
    protected final EditEntryBodyComponents components = new EditEntryBodyComponents();

    public EditEntryBody(EditEntry parent) {
        this.parent = parent;

        // Paint
        setLayout(new MigLayout("fillx, insets 15 15 15 15"));
        setBackground(Color.WHITE);

        // Instantiate
        components.recipientEmail.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 16));
        components.recipientEmailTF.setBorder(new LineBorder(Color.BLACK, 1, false));
        components.recipientEmailTF.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 15));
        components.recipientEmailPanel.setBackground(Color.WHITE);
        components.recipientEmailPanel.add(components.recipientEmail, "wrap");
        components.recipientEmailPanel.add(components.recipientEmailTF, "span, width 100%");

        components.subject.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 16));
        components.subjectTF.setBorder(new LineBorder(Color.BLACK, 1, false));
        components.subjectTF.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 15));
        components.subjectPanel.setBackground(Color.WHITE);
        components.subjectPanel.add(components.subject, "wrap");
        components.subjectPanel.add(components.subjectTF, "span, width 100%");

        components.body.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 16));
        components.bodyTA.setRows(10);
        components.bodyTA.setLineWrap(true);
        components.bodyTA.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 15));
        components.bodyPanel.setBackground(Color.WHITE);
        components.bodyPanel.add(components.body, "wrap");
        components.bodyPanel.add(components.bodyScroll, "span, width 100%");

        components.schedule.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 16));
        components.schedulePanel.setBackground(Color.WHITE);
        components.schedulePanel.add(components.schedule, "wrap");

        components.schedulePanel.add(components.scheduleDate, "width 100%, wrap");

        components.timePicker.setFont(Fonts.getLight().deriveFont(Font.PLAIN, 15));
        components.schedulePanel.add(components.timePicker, "width 100%");

        // Finalize
        add(components.recipientEmailPanel, "gapy 0 10, width 100%, wrap");
        add(components.subjectPanel, "gapy 0 10, width 100%, wrap");
        add(components.bodyPanel, "gapy 0 10, width 100%, wrap");
        add(components.schedulePanel, "width 100%");
    }
}

class EditEntryComponents {
    EditEntryHead head;
    EditEntryBody body;
}
public class EditEntry extends SmartJFrame {
    private Entry currentEntry;
    private final EditEntryComponents components = new EditEntryComponents();

    private final Dimension frameSize = new Dimension(500, 700);
    private Point startLocation;
    public final String startTitle = "Email Scheduler - Edit Entry";

    public EditEntry(MainFrame previous, String id) {
        super(previous);

        for (EntryTimer entryTimer : EntryHandler.entries)
            if (entryTimer.entry.id.equals(id)) {
                currentEntry = entryTimer.entry;
                break;
            }

        // Paint
        setLayout(new BorderLayout());
        setTitle(startTitle);
        setBackground(Color.WHITE);
        pack();
        setSize(frameSize);
        Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
        startLocation = new Point(screenDims.width / 2 - frameSize.width / 2, screenDims.height / 2 - frameSize.width / 2);
        setLocation(startLocation.x, startLocation.y);

        // Instantiate
        components.head = new EditEntryHead(this, new EditEntryHead.InteractionListener() {
            @Override
            public void onBack(EditEntryHead head) {
                pop();
            }

            @Override
            public void onSave(EditEntryHead head) {
                // Validate input
                if (components.body.components.recipientEmailTF.getText().length() == 0 || components.body.components.subjectTF.getText().length() == 0 ||
                        components.body.components.bodyTA.getText().length() == 0 || !components.body.components.scheduleDate.getModel().isSelected()) {
                    JOptionPane.showMessageDialog(EditEntry.this,
                            "Please fill all the fields before editing the entry.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!components.body.components.recipientEmailTF.getText().matches("[a-z0-9._]+@[a-z0-9._]+")) {
                    JOptionPane.showMessageDialog(EditEntry.this,
                            "Enter a valid email address.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Save entry

                Calendar scheduleDateSelected = Calendar.getInstance();
                scheduleDateSelected.setTimeZone(TimeZone.getDefault());
                scheduleDateSelected.setTime(((UtilDateModel) components.body.components.scheduleDate.getModel()).getValue());
                scheduleDateSelected.set(Calendar.HOUR_OF_DAY, components.body.components.timePicker.getTime().getHour());
                scheduleDateSelected.set(Calendar.MINUTE, components.body.components.timePicker.getTime().getMinute());
                scheduleDateSelected.set(Calendar.SECOND, 0);

                Calendar temp = Calendar.getInstance();
                temp.setTimeZone(TimeZone.getDefault());
                temp.setTime(new Date());

                if (scheduleDateSelected.before(temp)) {
                    // Date cannot be old

                    JOptionPane.showMessageDialog(EditEntry.this,
                            "Please choose a valid date/time.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                currentEntry.setRecipientEmail(components.body.components.recipientEmailTF.getText());
                currentEntry.setSubject(components.body.components.subjectTF.getText());
                currentEntry.setBody(components.body.components.bodyTA.getText());

                currentEntry.setSchedule(scheduleDateSelected.getTimeInMillis());
                currentEntry.setPendingStatus(true);

                try {
                    EntryHandler.save();
                    EntryHandler.registerTimers(currentEntry.id);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

                JOptionPane.showMessageDialog(EditEntry.this,
                        "Entry saved",
                        "Alert",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh and Close this JFrame
                dispose();
            }
        });
        components.body = new EditEntryBody(this);

        // Set entry details
        components.body.components.recipientEmailTF.setText(currentEntry.getRecipientEmail());
        components.body.components.subjectTF.setText(currentEntry.getSubject());
        components.body.components.bodyTA.setText(currentEntry.getBody());
        ((UtilDateModel) components.body.components.scheduleDate.getModel()).setValue(new Date(currentEntry.getSchedule()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentEntry.getSchedule()));
        components.body.components.timePicker.setTime(LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        // Finalize
        add(components.head, BorderLayout.PAGE_START);
        add(components.body, BorderLayout.CENTER);
    }
}