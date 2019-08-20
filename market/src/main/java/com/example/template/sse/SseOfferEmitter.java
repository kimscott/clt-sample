package com.example.template.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseOfferEmitter extends SseEmitter {

    private String username;
    private Long offerId;

    public SseOfferEmitter(String username, Long offerId) {
        super();
        this.username = username;
        this.offerId = offerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }
}
