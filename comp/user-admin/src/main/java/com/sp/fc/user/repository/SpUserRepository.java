package com.sp.fc.user.repository;

import com.sp.fc.user.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpUserRepository extends JpaRepository<SpUser, Long> {

    @Query("select u from SpUser u join fetch u.authorities where u.email = :email")
    Optional<SpUser> findByEmail(@Param("email") String email);
}
