package com.example.userapi.service;



import com.example.userapi.entity.User;
import java.util.List;

public interface UserService {
    List<User> getUsersBySupervisor(String supervisorUserId);
    User updateSupervisor(String userId, String newSupervisorUserId);
}
