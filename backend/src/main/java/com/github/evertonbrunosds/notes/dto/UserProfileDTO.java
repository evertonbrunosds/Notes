package com.github.evertonbrunosds.notes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.evertonbrunosds.notes.constraint.annotation.Birthday;
import com.github.evertonbrunosds.notes.constraint.annotation.DisplayName;
import com.github.evertonbrunosds.notes.constraint.annotation.Email;
import com.github.evertonbrunosds.notes.constraint.annotation.Password;
import com.github.evertonbrunosds.notes.constraint.annotation.UserName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {

    public static final String ENDPOINT = "/user_profile";

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @UserName
    private String userName;

    @Email
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String email;

    @DisplayName
    private String displayName;

    private String description;

    @Birthday
    private LocalDate birthday;

    @Password
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

}
