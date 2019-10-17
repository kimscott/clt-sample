package com.example.template;

import org.springframework.stereotype.Service;

@Service
public class AbstractEvent {

    String eventType;
    String timestamp;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isMe(){
        return getEventType().equals(getClass().getSimpleName());
    }
}
