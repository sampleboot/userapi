package com.example.userapi.repository;



import com.example.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findBySupervisorUserId(String supervisorUserId);
    User findByUserId(String userId);
}
