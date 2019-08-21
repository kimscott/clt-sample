package com.example.template.event;

import com.example.template.AbstractEvent;

public class SlotOffered  extends AbstractEvent {

    String name;
    Long offerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }
}
