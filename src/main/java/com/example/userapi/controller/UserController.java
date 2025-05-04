package com.example.userapi.controller;

import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userDTO) {
        log.info("Creating user: {}", userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userDTO));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserRequest userDTO) {
        log.info("Updating user {}: {}", userId, userDTO);
        UserResponse user = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.info("Deleting user: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/supervisor/{supervisorUserId}")
    public ResponseEntity<List<UserResponse>> getUsersBySupervisor(
            @PathVariable String supervisorUserId) {
        return ResponseEntity.ok(userService.getUsersBySupervisor(supervisorUserId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserByUserId(
            @PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserInfoByUserId(userId));
    }


    @PatchMapping("/{userId}/supervisor/{supervisorUserId}")
    public ResponseEntity<UserResponse> updateSupervisor(
            @PathVariable String userId,
            @PathVariable String supervisorUserId) {
        return ResponseEntity.ok(userService.updateSupervisor(userId, supervisorUserId));
    }
}
