package com.example.template.event;

public class OfferTaken  extends AbstractEvent{

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
