package com.arogueotaku.authentication.repositories;

import com.arogueotaku.authentication.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

//    @Query("SELECT u FROM User u WHERE u.username = ?1 OR u.email = ?1")
    User findByUsernameOrEmail(String username, String email);
}
