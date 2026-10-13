package com.dati.auth.authentication;

import lombok.Data;

@Data
public class User {

    private String id;

    private String name;

    private String displayName;

    @com.fasterxml.jackson.annotation.JsonProperty("is_guest")
    public boolean isGuest() {
        return id != null && id.startsWith("guest-");
    }

}
