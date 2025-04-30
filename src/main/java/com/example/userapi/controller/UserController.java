package com.example.userapi.controller;



import com.example.userapi.entity.User;
import com.example.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/supervisor/{supervisorUserId}")
    public List<User> getUsersBySupervisor(@PathVariable String supervisorUserId) {
        return userService.getUsersBySupervisor(supervisorUserId);
    }

    @PatchMapping("/{userId}/supervisor")
    public User updateSupervisor(@PathVariable String userId, @RequestParam String supervisorUserId) {
        return userService.updateSupervisor(userId, supervisorUserId);
    }
}

