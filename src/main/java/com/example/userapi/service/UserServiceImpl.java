package com.example.userapi.service;

import com.example.userapi.dto.UserDTO;
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
    public UserDTO createUser(UserDTO userDTO) {

        User existing=getUserByUserId(userDTO.getUserId());
        if(!ObjectUtils.isEmpty(existing)){
            throw new DuplicateUserFoundException("user already exists with userId");
        }
        // Map UserDTO to User entity
        User user = userMapper.toEntity(userDTO);

        // Set user-related fields and timestamps
        user.setCreateDttm(LocalDateTime.now());
        user.setUpdateDttm(LocalDateTime.now());

        // Handle addresses (set bi-directional relationship)
        if (userDTO.getAddresses() != null) {
            List<Address> addresses = addressMapper.toEntities(userDTO.getAddresses());
            addresses.forEach(address -> address.setUser(user));  // Manually set the 'user' field
            user.setAddresses(addresses);
        }
        return userMapper.toDTO(userRepository.save(user));

    }

    @Override
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        User existingUser = getUserByUserId(userId);
        if(ObjectUtils.isEmpty(existingUser) ){
            throw new UserNotFoundException("User with UserId " + userId + " not found");
        }
        // Use MapStruct to update simple fields
        userMapper.updateUserFromDto(userDTO, existingUser);
        existingUser.setUpdateDttm(LocalDateTime.now());

        // Handle address updates manually
        if (userDTO.getAddresses() != null) {
            // Clear old and re-add updated addresses
            existingUser.getAddresses().clear();
            List<Address> updatedAddresses = addressMapper.toEntities(userDTO.getAddresses());
            updatedAddresses.forEach(address -> address.setUser(existingUser));
            existingUser.getAddresses().addAll(updatedAddresses);
        }
        return userMapper.toDTO(userRepository.save(existingUser));


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
    public List<UserDTO> getUsersBySupervisor(String supervisorUserId) {
        List<User> users = userRepository.findBySupervisorUserId(supervisorUserId);
        log.info("users by supervisor"+users);
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateSupervisor(String userId, String supervisorUserId) {
        User user = userRepository.findByUserId(userId);
        User supervisor = userRepository.findByUserId(supervisorUserId);
        if(ObjectUtils.isEmpty(user) ){
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        if(ObjectUtils.isEmpty(supervisor) ){
            throw new UserNotFoundException("SuperVisor with ID " + userId + " not found");
        }
        user.setSupervisorUserId(supervisorUserId);
        return userMapper.toDTO(userRepository.save(user));
    }



    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

}
