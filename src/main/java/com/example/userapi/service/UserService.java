package com.example.userapi.service;

import com.example.userapi.dto.UserDTO;
import com.example.userapi.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(String userId, User user);
    void deleteUser(String userId);
    List<UserDTO> getUsersBySupervisor(String supervisorUserId);
    User updateSupervisor(String userId, String supervisorUserId);


    }





