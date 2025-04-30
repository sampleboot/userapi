package com.example.userapi.service;



import com.example.userapi.entity.User;
import com.example.userapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getUsersBySupervisor(String supervisorUserId) {
        return userRepository.findBySupervisorUserId(supervisorUserId);
    }

    @Override
   public User updateSupervisor(String userId, String newSupervisorUserId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            user.setSupervisorUserId(newSupervisorUserId);
            return userRepository.save(user);
        }
        return null;


    }
}

