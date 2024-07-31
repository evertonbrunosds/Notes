package com.github.evertonbrunosds.notes.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.evertonbrunosds.notes.constraint.annotation.Birthday;
import com.github.evertonbrunosds.notes.constraint.annotation.DisplayName;
import com.github.evertonbrunosds.notes.constraint.annotation.Email;
import com.github.evertonbrunosds.notes.constraint.annotation.Password;
import com.github.evertonbrunosds.notes.constraint.annotation.UserName;
import com.github.evertonbrunosds.notes.orm.UserProfileORM;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfileDTO {

    public static final String ENDPOINT = "/user_profile";

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @UserName
    private String userName;

    @Email
    // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String email;

    @DisplayName
    private String displayName;
    
    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @Birthday
    private LocalDate birthday;

    @Password
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    public UserProfileORM toORM() {
        final var orm = new UserProfileORM();
        orm.setUserName(userName);
        orm.setEmail(email);
        orm.setDisplayName(displayName);
        orm.setDescription(description);
        orm.setBirthday(birthday);
        orm.setPassword(password);
        return orm;
    }

}
