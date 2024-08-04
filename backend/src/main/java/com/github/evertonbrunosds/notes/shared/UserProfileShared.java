package com.github.evertonbrunosds.notes.shared;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfileShared {

    public static final String ENDPOINT = "/user_profile";

    private UUID id;

    private String userName;

    private String email;

    private String displayName;
    
    private String description;

    private LocalDate birthday;

    private String password;

    private LocalDateTime createdAt;

}
