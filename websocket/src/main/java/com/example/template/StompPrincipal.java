package com.example.template;

import java.security.Principal;

class StompPrincipal implements Principal {
    String name;
    String role;

    StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}