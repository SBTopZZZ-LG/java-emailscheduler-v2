package Frames;

import Models.Entry;
import Models.EntryHandler;
import Models.EntryTimer;
import Utilities.Fonts;
import Utilities.HeadButton;
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

class EditEntryHead extends JPanel {
    // Components
    private HeadButton backButton;
    private HeadButton saveButton;

    // Component data
    private EditEntry parent;
    private int headSize = 50;
    private Point headLocation = new Point(0, 0);
    public interface InteractionListener {
        void onBack(EditEntryHead head);
        void onSave(EditEntryHead head);
    }

    public EditEntryHead(EditEntry parent, InteractionListener listener) {
        this.parent = parent;

        // Set properties
        setSize(parent.getSize().width, headSize);
        setLocation(headLocation.x, headLocation.y);
        setBackground(Color.WHITE);
        setLayout(new MigLayout("insets 5 10 5 10, fillx"));

        // Instantiate
        backButton = new HeadButton(this, "Go Back", "res/images/back.png",
                new HeadButton.InteractionListener() {
                    @Override
                    public void onClick() {
                        listener.onBack(EditEntryHead.this);
                    }
                });
        saveButton = new HeadButton(this, "Save entry", "res/images/save.png",
                new HeadButton.InteractionListener() {
                    @Override
                    public void onClick() {
                        listener.onSave(EditEntryHead.this);
                    }
                });

        // Finalize
        add(backButton, "width 80%");
        add(saveButton, "width 80%");
    }
}

class EditEntryBody extends JPanel {
    // Components
    private JPanel recipientEmailPanel;
    private JPanel subjectPanel;
    private JPanel bodyPanel;
    private JPanel schedulePanel;

    private JScrollPane bodyScroll;

    private JLabel recipientEmail;
    private JLabel subject;
    private JLabel body;
    private JLabel schedule;

    JTextField recipientEmailTF;
    JTextField subjectTF;
    JTextArea bodyTA;

    JDatePanelImpl scheduleDate;
    TimePicker timePicker;

    // Component data
    private EditEntry parent;

    public EditEntryBody(EditEntry parent) {
        this.parent = parent;

        // Instantiate
        recipientEmailPanel = new JPanel(new MigLayout("fillx, insets 0 0 0 0"));
        recipientEmail = new JLabel("Recipient's Email Address");
        recipientEmail.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 15));
        recipientEmailTF = new JTextField();
        recipientEmailTF.setBorder(new LineBorder(Color.BLACK, 1, false));
        recipientEmailPanel.setBackground(Color.WHITE);
        recipientEmailPanel.add(recipientEmail, "wrap");
        recipientEmailPanel.add(recipientEmailTF, "span, width 100%");

        subjectPanel = new JPanel(new MigLayout("fillx, insets 0 0 0 0"));
        subject = new JLabel("Subject");
        subject.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 15));
        subjectTF = new JTextField();
        subjectTF.setBorder(new LineBorder(Color.BLACK, 1, false));
        subjectTF.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 15));
        subjectPanel.setBackground(Color.WHITE);
        subjectPanel.add(subject, "wrap");
        subjectPanel.add(subjectTF, "span, width 100%");

        bodyPanel = new JPanel(new MigLayout("fillx, insets 0 0 0 0"));
        body = new JLabel("Body");
        body.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 15));
        bodyTA = new JTextArea();
        bodyTA.setRows(10);
        bodyTA.setLineWrap(true);
        bodyTA.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 15));
        bodyScroll = new JScrollPane(bodyTA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.add(body, "wrap");
        bodyPanel.add(bodyScroll, "span, width 100%");

        schedulePanel = new JPanel(new MigLayout("fillx, insets 0 0 0 0"));
        schedule = new JLabel("Schedule at");
        schedule.setFont(Fonts.getRegular().deriveFont(Font.PLAIN, 15));
        schedulePanel.setBackground(Color.WHITE);
        schedulePanel.add(schedule, "wrap");
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        scheduleDate = new JDatePanelImpl(new UtilDateModel(), p);
        schedulePanel.add(scheduleDate, "width 100%, wrap");
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.setColor(TimePickerSettings.TimeArea.TimePickerTextValidTime, Color.black);
        timeSettings.initialTime = LocalTime.now();
        timePicker = new TimePicker(timeSettings);
        schedulePanel.add(timePicker, "width 100%");

        // Set properties
        setLayout(new MigLayout("fillx, insets 15 15 15 15"));
        setBackground(Color.WHITE);

        // Finalize
        add(recipientEmailPanel, "gapy 0 10, width 100%, wrap");
        add(subjectPanel, "gapy 0 10, width 100%, wrap");
        add(bodyPanel, "gapy 0 10, width 100%, wrap");
        add(schedulePanel, "width 100%");
    }
}

public class EditEntry extends SmartJFrame {
    // Components
    private EditEntryHead head;
    private EditEntryBody body;

    // Component data
    private Entry currentEntry;
    private Dimension frameSize = new Dimension(500, 700);
    private Point startLocation;
    private final String startTitle = "Email Scheduler - Edit Entry";

    public EditEntry(MainFrame previous, String id) {
        super(previous);

        for (EntryTimer entryTimer : EntryHandler.entries)
            if (entryTimer.entry.id.equals(id)) {
                currentEntry = entryTimer.entry;
                break;
            }

        // Instantiate
        head = new EditEntryHead(this, new EditEntryHead.InteractionListener() {
            @Override
            public void onBack(EditEntryHead head) {
                // Display previous window then dispose current one
                previous.display();
                dispose();
            }

            @Override
            public void onSave(EditEntryHead head) {
                // Validate input
                if (body.recipientEmailTF.getText().length() == 0 || body.subjectTF.getText().length() == 0 ||
                        body.bodyTA.getText().length() == 0 || !body.scheduleDate.getModel().isSelected()) {
                    JOptionPane.showMessageDialog(EditEntry.this,
                            "Please fill all the fields before editing the entry.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!body.recipientEmailTF.getText().matches("[a-z0-9._]+@[a-z0-9._]+")) {
                    JOptionPane.showMessageDialog(EditEntry.this,
                            "Enter a valid email address.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Save entry

                Calendar scheduleDateSelected = Calendar.getInstance();
                scheduleDateSelected.setTimeZone(TimeZone.getDefault());
                scheduleDateSelected.setTime(((UtilDateModel) body.scheduleDate.getModel()).getValue());
                scheduleDateSelected.set(Calendar.HOUR_OF_DAY, body.timePicker.getTime().getHour());
                scheduleDateSelected.set(Calendar.MINUTE, body.timePicker.getTime().getMinute());
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

                currentEntry.setRecipientEmail(body.recipientEmailTF.getText());
                currentEntry.setSubject(body.subjectTF.getText());
                currentEntry.setBody(body.bodyTA.getText());

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
        body = new EditEntryBody(this);

        // Set frame properties
        setLayout(new BorderLayout());
        setTitle(startTitle);
        setBackground(Color.WHITE);
        pack();
        setLocationRelativeTo(null); // Sets position to center
        setSize(frameSize);
        Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
        startLocation = new Point(screenDims.width / 2 - frameSize.width / 2, screenDims.height / 2 - frameSize.width / 2);
        setLocation(startLocation.x, startLocation.y);

        // Set entry details
        body.recipientEmailTF.setText(currentEntry.getRecipientEmail());
        body.subjectTF.setText(currentEntry.getSubject());
        body.bodyTA.setText(currentEntry.getBody());
        ((UtilDateModel) body.scheduleDate.getModel()).setValue(new Date(currentEntry.getSchedule()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentEntry.getSchedule()));
        body.timePicker.setTime(LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        add(head, BorderLayout.PAGE_START);
        add(body, BorderLayout.CENTER);
    }
}
