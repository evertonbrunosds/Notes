package com.github.evertonbrunosds.notes.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.evertonbrunosds.notes.orm.UserProfileORM;

public interface UserProfileRepository extends JpaRepository<UserProfileORM, UUID> {

    Page<UserProfileORM> findByUserNameContaining(final String userName, final Pageable pageable);

    Optional<UserProfileORM> findByEmail(final String email);

    Optional<UserProfileORM> findByUserName(final String userName);

}
