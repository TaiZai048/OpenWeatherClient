package cn.aetherial.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class Alerts {

    private String senderName;
    private String event;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private String tags;
    
    public Alerts() {
    }
    
    public Alerts(String senderName, String event, LocalDateTime start, LocalDateTime end, String description, String tags) {
        this.senderName = senderName;
        this.event = event;
        this.start = start;
        this.end = end;
        this.description = description;
        this.tags = tags;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setStart(Long timestamp) {
        if (timestamp != null) {
            this.start = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        } else {
            this.start = null;
        }
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setEnd(Long timestamp) {
        if (timestamp != null) {
            this.end = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
        } else {
            this.end = null;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alerts alerts = (Alerts) o;
        return Objects.equals(senderName, alerts.senderName) &&
               Objects.equals(event, alerts.event) &&
               Objects.equals(start, alerts.start) &&
               Objects.equals(end, alerts.end) &&
               Objects.equals(description, alerts.description) &&
               Objects.equals(tags, alerts.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderName, event, start, end, description, tags);
    }

    @Override
    public String toString() {
        return "Alerts{" +
                "senderName='" + senderName + '\'' +
                ", event='" + event + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }

}
