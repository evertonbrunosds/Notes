package com.github.evertonbrunosds.notes.orm;

import static com.github.evertonbrunosds.notes.util.LocalDateTimeManager.currentLocalDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_profile")
public class UserProfileORM {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "user_name", length = 32, nullable = false, unique = true)
    private String userName;

    @Column(name = "email", length = 256, nullable = false, unique = true)
    private String email;

    @Column(name = "display_name", length = 32, nullable = false)
    private String displayName;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Setter(value = AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserProfileORM() {
        createdAt = currentLocalDateTime();
    }

}
