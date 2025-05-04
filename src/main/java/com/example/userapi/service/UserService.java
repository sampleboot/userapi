package com.example.userapi.service;

import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponse createUser(UserRequest userDTO);

    UserResponse updateUser(String userId, UserRequest userDTO);

    void deleteUser(String userId);

    List<UserResponse> getUsersBySupervisor(String supervisorUserId);

    UserResponse updateSupervisor(String userId, String supervisorUserId);

    User getUserByUserId(String userId);

    UserResponse getUserInfoByUserId(String userId);

    Optional<User> getUserById(Integer id);

}
