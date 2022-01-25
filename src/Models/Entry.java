package Models;

import java.util.Map;
import java.util.UUID;

public class Entry {
    public final String id;
    private String recipientEmail;
    private String subject;
    private String body;
    private long schedule;
    private boolean isPending;

    public Entry() {
        id = UUID.randomUUID().toString();
    }
    public Entry(Map<String, Object> map) {
        id = map.getOrDefault("id", UUID.randomUUID().toString()).toString();
        recipientEmail = map.get("recipientEmail").toString();
        subject = map.get("subject").toString();
        body = map.get("body").toString();
        this.schedule = Long.parseLong(map.get("schedule").toString().split("E")[0].replace(".", ""));
        isPending = (boolean) map.get("isPending");
    }
    public Entry(String recipientEmail, String subject, String body, long schedule, boolean isPending) {
        id = UUID.randomUUID().toString();
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.schedule = schedule;
        this.isPending = isPending;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }
    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public long getSchedule() {
        return schedule;
    }
    public void setSchedule(long scheduleDate) {
        this.schedule = scheduleDate;
    }

    public boolean isPending() {
        return isPending;
    }
    public void setPendingStatus(boolean isPending) {
        this.isPending = isPending;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id='" + id + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", schedule=" + schedule +
                ", isPending=" + isPending +
                '}';
    }
}
