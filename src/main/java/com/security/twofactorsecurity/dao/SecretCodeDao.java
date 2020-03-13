package com.security.twofactorsecurity.dao;

import com.security.twofactorsecurity.model.SecretCode;
import com.security.twofactorsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecretCodeDao extends JpaRepository<SecretCode, Long> {
    Optional<SecretCode> findByUser(User user);
}
