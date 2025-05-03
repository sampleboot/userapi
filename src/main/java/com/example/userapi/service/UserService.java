package com.example.userapi.service;

import com.example.userapi.dto.UserDTO;
import com.example.userapi.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(String userId, UserDTO userDTO);
    void deleteUser(String userId);
    List<UserDTO> getUsersBySupervisor(String supervisorUserId);
    UserDTO updateSupervisor(String userId, String supervisorUserId);
    User getUserByUserId(String userId);
    Optional<User> getUserById(Integer id);

    }





