package com.security.auth.repositary;

import com.security.auth.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<Users,Long> {

    //    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<Users> findByEmail(String email);

    //    @Query("SELECT u FROM User u WHERE u.mobile = ?1")
    Optional<Users> findByMobile(String mobile);
}
