package com.example.userapi.controller;

import com.example.userapi.dto.UserRequest;
import com.example.userapi.dto.UserResponse;
import com.example.userapi.entity.User;
import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User sampleUser;
    private UserResponse sampleUserResponse;
    private UserRequest sampleUserRequest;
    private UserRequest invalidUserDTO;
    String userNotFoundId = "userIdNotFound";

    @BeforeEach
    public void setUp() {
        sampleUserRequest = new UserRequest();
        sampleUserRequest.setUserId("user123");
        sampleUserRequest.setFirstName("John");
        sampleUserRequest.setLastName("Smith");
        sampleUserRequest.setEmailAddress("john.smith@example.com");
        sampleUserRequest.setSupervisorUserId("sup1");
        sampleUserRequest.setCreateUserId("testuser");


        sampleUserResponse = new UserResponse();
        sampleUserResponse.setUserId("user123");
        sampleUserResponse.setFirstName("John");
        sampleUserResponse.setLastName("Smith");
        sampleUserResponse.setEmailAddress("john.smith@example.com");
        sampleUserResponse.setSupervisorUserId("sup1");

        invalidUserDTO = new UserRequest();

        invalidUserDTO.setUserId(null);
        invalidUserDTO.setFirstName(RandomStringUtils.secureStrong().nextAlphabetic(200));
        invalidUserDTO.setLastName(RandomStringUtils.secureStrong().nextAlphabetic(200));
        invalidUserDTO.setEmailAddress(RandomStringUtils.secureStrong().nextAlphabetic(200));
        invalidUserDTO.setSupervisorUserId(RandomStringUtils.secureStrong().nextAlphabetic(200));

    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateUser() throws Exception {
        Mockito.when(userMapper.toUserResponseDTO(sampleUser)).thenReturn(sampleUserResponse);
        Mockito.when(userService.createUser(any(UserRequest.class))).thenReturn(sampleUserResponse);


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user123"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateUserWithValidationErrors() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.firstName").isNotEmpty())
                .andExpect(jsonPath("$.emailAddress").isNotEmpty())
                .andExpect(jsonPath("$.lastName").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testUpdateUser() throws Exception {
        Mockito.when(userMapper.toUserResponseDTO(sampleUser)).thenReturn(sampleUserResponse);
        Mockito.when(userService.updateUser(eq("user123"), any(UserRequest.class))).thenReturn(sampleUserResponse);


        mockMvc.perform(put("/users/user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"));
    }


    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testUpdateUser_throwExceptionWhenUserIdNotFound() throws Exception {
        Mockito.when(userService.updateUser(userNotFoundId, sampleUserRequest)).thenThrow(UserNotFoundException.class);
        mockMvc.perform(put("/users/" + userNotFoundId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserRequest)))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testUpdateUser_throwGeneralException() throws Exception {
        Mockito.when(userService.updateUser(userNotFoundId, sampleUserRequest)).thenThrow(RuntimeException.class);
        mockMvc.perform(put("/users/" + userNotFoundId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserRequest)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").doesNotExist());

    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("user123");

        mockMvc.perform(delete("/users/user123"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testGetUsersBySupervisor() throws Exception {
        Mockito.when(userService.getUsersBySupervisor("sup1"))
                .thenReturn(Collections.singletonList(sampleUserResponse));

        mockMvc.perform(get("/users/supervisor/sup1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user123"));
    }


    @WithMockUser(username = "user", roles = "USER")
    public void testUpdateSupervisor() throws Exception {
        sampleUser.setSupervisorUserId("sup2");
        sampleUserResponse.setSupervisorUserId("sup2");
        Mockito.when(userMapper.toUserResponseDTO(sampleUser)).thenReturn(sampleUserResponse);
        Mockito.when(userService.getUserInfoByUserId("user123")).thenReturn(sampleUserResponse);
        Mockito.when(userService.updateSupervisor("user123", "sup2")).thenReturn(sampleUserResponse);

        mockMvc.perform(patch("/users/user123/supervisor/sup2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supervisorUserId").value("sup2"));
    }
}
