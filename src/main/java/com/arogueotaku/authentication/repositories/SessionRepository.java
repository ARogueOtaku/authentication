package com.arogueotaku.authentication.repositories;

import com.arogueotaku.authentication.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Modifying
    @Transactional
    List<Session> deleteByRefreshToken(String refreshToken);

    Optional<Session> findByRefreshToken(String refreshToken);
}
