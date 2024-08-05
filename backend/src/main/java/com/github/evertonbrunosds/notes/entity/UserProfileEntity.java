package com.github.evertonbrunosds.notes.entity;

import static com.github.evertonbrunosds.notes.util.LocalDateTimeManager.currentLocalDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnTransformer;

import com.github.evertonbrunosds.notes.validation.annotation.Birthday;
import com.github.evertonbrunosds.notes.validation.annotation.Description;
import com.github.evertonbrunosds.notes.validation.annotation.DisplayName;
import com.github.evertonbrunosds.notes.validation.annotation.Email;
import com.github.evertonbrunosds.notes.validation.annotation.Password;
import com.github.evertonbrunosds.notes.validation.annotation.UserName;

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
public class UserProfileEntity {

    private static final String KEY = "Xd4oEGeTkRgYaXHfeEsh8AXuf3OIkEUo";

    private static final String HOW_READ = "pgp_sym_decrypt(email::bytea, '" + KEY + "')";

    private static final String HOW_WRITE = "pgp_sym_encrypt(?, '" + KEY + "')";

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @UserName
    @Column(name = "user_name", length = 32, nullable = false, unique = true)
    private String userName;

    @Email
    @ColumnTransformer(read = HOW_READ, write = HOW_WRITE)
    @Column(name = "email", length = 262, nullable = false, unique = true)
    private String email;

    @DisplayName
    @Column(name = "display_name", length = 32, nullable = false)
    private String displayName;

    @Description
    @Column(name = "description", length = 1024)
    private String description;

    @Birthday
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Password
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Setter(value = AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserProfileEntity() {
        createdAt = currentLocalDateTime();
    }

}
