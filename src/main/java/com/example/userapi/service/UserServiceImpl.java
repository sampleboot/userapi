package com.example.userapi.service;

import com.example.userapi.entity.User;
import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String userId, User user) {
        User existingUser = userRepository.findByUserId(userId);
        if(ObjectUtils.isEmpty(existingUser) ){
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmailAddress(user.getEmailAddress());
        existingUser.setTitleText(user.getTitleText());
        existingUser.setSupervisorUserId(user.getSupervisorUserId());
        existingUser.setUpdateUserId(user.getUpdateUserId());
        existingUser.setUpdateDttm(user.getUpdateDttm());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId);
        if(ObjectUtils.isEmpty(user) ){
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }

        userRepository.delete(user);
    }

    @Override
    public List<User> getUsersBySupervisor(String supervisorUserId) {
        return userRepository.findBySupervisorUserId(supervisorUserId);
    }

    @Override
    public User updateSupervisor(String userId, String supervisorUserId) {
        //Assuming Valid supervisorUserId is sent .it should also be validated
        User user = userRepository.findByUserId(userId);
        if(ObjectUtils.isEmpty(user) ){
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        user.setSupervisorUserId(supervisorUserId);
        return userRepository.save(user);
    }
}
