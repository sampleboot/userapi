package com.example.userapi.service;

import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.entity.User;
import com.example.userapi.exception.DuplicateUserFoundException;
import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.mapper.AddressMapper;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldCreateUser_WhenUserDoesNotExist() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserId("testUser");
        userRequest.setCreateUserId("createdtestuser");

        User user = new User();
        user.setUserId(userRequest.getUserId());

        User createdByUserInfo = new User();
        createdByUserInfo.setUserId("createdtestuser");

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId("testUser");

        when(userRepository.findByUserId("testUser")).thenReturn(null);
        when(userRepository.findByUserId("createdtestuser")).thenReturn(createdByUserInfo);
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(userResponse);
        UserResponse result = userService.createUser(userRequest);
        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void createUser_ShouldThrowException_WhenUserAlreadyExists() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserId("testUser");
        User existingUser = new User();
        when(userRepository.findByUserId("testUser")).thenReturn(existingUser);
        assertThrows(DuplicateUserFoundException.class, () -> userService.createUser(userRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() {
        String userId = "testUser";
        UserRequest userRequest = new UserRequest();
        userRequest.setUserId(userId);
        userRequest.setCreateUserId("updatedbytestuser");
        User updatedByUserInfo = new User();
        updatedByUserInfo.setUserId("updatedbytestuser");

        User existingUser = new User();
        existingUser.setUserId(userId);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId("testUser");

        when(userRepository.findByUserId(userId)).thenReturn(existingUser);
        when(userRepository.findByUserId("updatedbytestuser")).thenReturn(updatedByUserInfo);
        when(userMapper.toEntity(userRequest)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toUserResponseDTO(existingUser)).thenReturn(userResponse);

        UserResponse result = userService.updateUser(userId, userRequest);

        assertNotNull(result);
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        String userId = "testUser";
        UserRequest userRequest = new UserRequest();
        when(userRepository.findByUserId(userId)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userId, userRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        String userId = "testUser";
        User user = new User();
        when(userRepository.findByUserId(userId)).thenReturn(user);
        userService.deleteUser(userId);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        String userId = "testUser";
        when(userRepository.findByUserId(userId)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, never()).delete(any());
    }

    @Test
    void getUsersBySupervisor_ShouldReturnUsers_WhenSupervisorExists() {
        String supervisorUserId = "supervisor1";
        User user = new User();
        UserRequest userRequest = new UserRequest();
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId("testUser");
        userResponse.setSupervisorUserId(supervisorUserId);
        when(userRepository.findBySupervisorUserId(supervisorUserId)).thenReturn(List.of(user));
        when(userMapper.toUserResponseDTO(user)).thenReturn(userResponse);
        List<UserResponse> result = userService.getUsersBySupervisor(supervisorUserId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUserId());
    }

    @Test
    void updateSupervisor_ShouldThrowException_WhenUserDoesNotExist() {
        String userId = "testUser";
        String supervisorUserId = "supervisor1";
        when(userRepository.findByUserId(userId)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.updateSupervisor(userId, supervisorUserId));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateSupervisor_ShouldThrowException_WhenSupervisorDoesNotExist() {
        String userId = "testUser";
        String supervisorUserId = "supervisor1";
        User user = new User();

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(userRepository.findByUserId(supervisorUserId)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.updateSupervisor(userId, supervisorUserId));
        verify(userRepository, never()).save(any());
    }
}