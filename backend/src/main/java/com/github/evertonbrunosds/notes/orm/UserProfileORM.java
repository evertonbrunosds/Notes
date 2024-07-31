package com.github.evertonbrunosds.notes.orm;

import static com.github.evertonbrunosds.notes.util.LocalDateTimeManager.currentLocalDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnTransformer;

import com.github.evertonbrunosds.notes.dto.UserProfileDTO;

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

    private static final String KEY = "Xd4oEGeTkRgYaXHfeEsh8AXuf3OIkEUo";

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "user_name", length = 32, nullable = false, unique = true)
    private String userName;

    @ColumnTransformer(
        read = "pgp_sym_decrypt(email::bytea, '" + KEY + "')",
        write = "pgp_sym_encrypt(?, '" + KEY + "')"
    )
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

    public UserProfileDTO toDTO() {
        final var dto = new UserProfileDTO();
        dto.setId(id);
        dto.setUserName(userName);
        dto.setEmail(email);
        dto.setDisplayName(displayName);
        dto.setDescription(description);
        dto.setBirthday(birthday);
        dto.setPassword(password);
        dto.setCreatedAt(createdAt);
        return dto;
    }

    public void load(final UserProfileDTO orm) {
        this.id = orm.getId();
        this.userName = orm.getUserName();
        this.email = orm.getEmail();
        this.displayName = orm.getDisplayName();
        this.description = orm.getDescription();
        this.birthday = orm.getBirthday();
        this.password = orm.getPassword();
    }

}
