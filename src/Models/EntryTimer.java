package Models;

import java.util.Timer;

public class EntryTimer {
    public Entry entry;
    public Timer timer;

    public EntryTimer() {}
    public EntryTimer(Entry entry) {
        this.entry = entry;
    }
    public EntryTimer(Entry entry, Timer timer) {
        this.entry = entry;
        this.timer = timer;
    }
}
