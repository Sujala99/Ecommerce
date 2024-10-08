package com.example.ebookstore.Repository;

import com.example.ebookstore.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>  {
    boolean existsByUsernameOrEmail(String username, String email);

    // Add additional query methods if needed
    Optional<Users> findByUsernameOrEmail(String username,String email);
//
//    //forgot pass

//    Optional<Users> findByEmail(String email);
//    Optional<Users> findByResetToken(String resetToken);


}
