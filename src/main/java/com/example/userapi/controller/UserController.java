package com.example.userapi.controller;

import com.example.userapi.entity.User;
import com.example.userapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    // Update a user by userId
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        User updated = userService.updateUser(userId, user);
        return ResponseEntity.ok(updated);
    }

    // Delete a user by userId
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Get users under a supervisor
    @GetMapping("/supervisor/{supervisorUserId}")
    public ResponseEntity<List<User>> getUsersBySupervisor(@PathVariable String supervisorUserId) {
        return ResponseEntity.ok(userService.getUsersBySupervisor(supervisorUserId));
    }

    // Update a user's supervisor
    @PutMapping("/{userId}/supervisor/{supervisorUserId}")
    public ResponseEntity<User> updateSupervisor(@PathVariable String userId, @PathVariable String supervisorUserId) {
        return ResponseEntity.ok(userService.updateSupervisor(userId, supervisorUserId));
    }
}
