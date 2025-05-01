package com.example.userapi.repository;


import com.example.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.supervisorUserId = :supervisorUserId")
    List<User> findBySupervisorUserId(String supervisorUserId);
    User findByUserId(String userId);
}
