package com.github.evertonbrunosds.notes.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.evertonbrunosds.notes.entity.UserProfileEntity;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, UUID> {

    Page<UserProfileEntity> findByUserNameContaining(final String userName, final Pageable pageable);

    Optional<UserProfileEntity> findByEmail(final String email);

    Optional<UserProfileEntity> findByUserName(final String userName);

}
