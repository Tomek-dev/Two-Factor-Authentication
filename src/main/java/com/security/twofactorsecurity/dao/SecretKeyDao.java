package com.security.twofactorsecurity.dao;

import com.security.twofactorsecurity.model.SecretKey;
import com.security.twofactorsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecretKeyDao extends JpaRepository<SecretKey, Long> {
    Optional<SecretKey> findByUser(User user);
}
