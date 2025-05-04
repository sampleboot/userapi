package com.example.userapi.service;

import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.entity.Address;
import com.example.userapi.entity.User;
import com.example.userapi.exception.DuplicateUserFoundException;
import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.mapper.AddressMapper;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AddressMapper addressMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, AddressMapper addressMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
    }

    @Override
    public UserResponse createUser(UserRequest userDTO) {

        User existing = getUserByUserId(userDTO.getUserId());
        if (!ObjectUtils.isEmpty(existing)) {
            throw new DuplicateUserFoundException("user already exists with userId");
        }
        User createdUser = getUserByUserId(userDTO.getCreateUserId());
        // Map UserDTO to User entity
        if (ObjectUtils.isEmpty(createdUser)) {
            throw new UserNotFoundException("User with ID " + userDTO.getCreateUserId() + " not found");
        }

        User user = userMapper.toEntity(userDTO);
        user.setUpdateUserId(createdUser.getId());
        user.setCreateUserId(createdUser.getId());

        user.setCreateUserId(createdUser.getId());
        user.setUpdateUserId(createdUser.getId());

        // Set user-related fields and timestamps
        user.setCreateDttm(LocalDateTime.now());
        user.setUpdateDttm(LocalDateTime.now());

        // Handle addresses (set bi-directional relationship)
        if (userDTO.getAddresses() != null) {
            List<Address> addresses = addressMapper.toEntities(userDTO.getAddresses());
            addresses.forEach(address -> address.setUser(user)); // Manually set the 'user' field
            user.setAddresses(addresses);
        }
        return userMapper.toUserResponseDTO(userRepository.save(user));

    }

    @Override
    public UserResponse updateUser(String userId, UserRequest userDTO) {
        User existingUser = getUserByUserId(userId);
        if (ObjectUtils.isEmpty(existingUser)) {
            throw new UserNotFoundException("User with UserId " + userId + " not found");
        }
        // Use MapStruct to update simple fields
        userMapper.updateUserFromDto(userDTO, existingUser);
        existingUser.setUpdateDttm(LocalDateTime.now());

        User updatedByUser = getUserByUserId(userDTO.getCreateUserId());
        existingUser.setUpdateUserId(updatedByUser.getId());
        // Handle address updates manually
        if (userDTO.getAddresses() != null) {
            // Clear old and re-add updated addresses
            existingUser.getAddresses().clear();
            List<Address> updatedAddresses = addressMapper.toEntities(userDTO.getAddresses());
            updatedAddresses.forEach(address -> address.setUser(existingUser));
            existingUser.getAddresses().addAll(updatedAddresses);
        }
        return userMapper.toUserResponseDTO(userRepository.save(existingUser));


    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }

        userRepository.delete(user);
    }

    @Override
    public List<UserResponse> getUsersBySupervisor(String supervisorUserId) {
        List<User> users = userRepository.findBySupervisorUserId(supervisorUserId);
        log.info("users by supervisor" + users);
        return users.stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateSupervisor(String userId, String supervisorUserId) {
        User user = userRepository.findByUserId(userId);
        User supervisor = userRepository.findByUserId(supervisorUserId);
        if (ObjectUtils.isEmpty(user)) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        if (ObjectUtils.isEmpty(supervisor)) {
            throw new UserNotFoundException("SuperVisor with ID " + userId + " not found");
        }
        user.setSupervisorUserId(supervisorUserId);
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }


    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public UserResponse getUserInfoByUserId(String userId) {
        UserResponse user = userMapper.toUserResponseDTO(userRepository.findByUserId(userId));
        if (ObjectUtils.isEmpty(user)) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        return user;
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

}
