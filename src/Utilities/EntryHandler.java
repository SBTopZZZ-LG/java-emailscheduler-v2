package Utilities;

import Models.Entry;
import Models.EntryTimer;
import com.cedarsoftware.util.io.JsonObject;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class EntryHandler {
    public interface Callback {
        void updated();
    }

    static final String PATH = "data.json";

    public static List<EntryTimer> entries = new ArrayList<>();
    public static List<Callback> callbacks = new ArrayList<>();

    public static void initialize() throws IOException {
        // Check if file exists
        if (Files.exists(Paths.get(PATH))) {
            // Load data from file
            String content = new String(Files.readAllBytes(Paths.get(PATH)));

            JsonObject parsed = (JsonObject) (JsonReader.jsonToMaps(content));
            var entries = (Object[]) parsed.getArray();
            if (entries != null)
                for (Object entry : entries) {
                    Map<String, Object> entryMap = (Map<String, Object>) entry;

                    EntryHandler.entries.add(new EntryTimer(new Entry(entryMap)));
                }
        } else {
            // Create empty file
            Files.writeString(Paths.get(PATH), "{\"@type\":\"java.util.ArrayList\"}");
        }
    }
    public static void unregisterTimers() {
        for (EntryTimer entryTimer : entries) {
            if (entryTimer.timer == null || !entryTimer.entry.isPending())
                continue;

            entryTimer.timer.cancel();
            entryTimer.timer = null;
        }
    }
    public static void registerTimers(String id) {
        for (EntryTimer entryTimer : entries) {
            if ((entryTimer.timer != null && entryTimer.entry.id.equals(id)) || !entryTimer.entry.isPending()) // Already set
                continue;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getDefault());
            calendar.setTime(new Date());

            // Check dispose timer object
            if (entryTimer.timer != null) {
                entryTimer.timer.cancel();
                entryTimer.timer = null;
            }
            entryTimer.timer = new Timer();
            long diff = entryTimer.entry.getSchedule() - calendar.getTimeInMillis();
            if (diff < 0) {
                // Timer surpassed (mark as Done)

                entryTimer.entry.setPendingStatus(false);
            } else {
                entryTimer.timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // Send email here!

                        Entry entry = entryTimer.entry;
                        try {
                            GoogleAuth.sendEmail(entry.getRecipientEmail(), entry.getSubject(), entry.getBody());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        entryTimer.entry.setPendingStatus(false);
                        try {
                            save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        notifyChange();

                        entryTimer.timer = null;
                    }
                }, diff);
            }
        }
        notifyChange();
    }
    public static void save() throws IOException {
        Files.writeString(Paths.get(PATH), JsonWriter.objectToJson(map()));
    }
    public static void notifyChange() {
        for (Callback callback : callbacks)
            callback.updated();
    }

    private static List<Entry> map() {
        List<Entry> entries = new ArrayList<>();
        for (EntryTimer entryTimer : EntryHandler.entries)
            entries.add(entryTimer.entry);
        return entries;
    }
}