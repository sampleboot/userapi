package com.example.userapi.controller;

import com.example.userapi.dto.UserDTO;
import com.example.userapi.entity.User;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private User sampleUser;
    private UserDTO sampleUserDTO;

    @BeforeEach
    public void setUp() {
        sampleUser = new User();
        sampleUser.setUserId("user123");
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Smith");
        sampleUser.setEmailAddress("john.smith@example.com");
        sampleUser.setSupervisorUserId("sup1");



        sampleUserDTO = new UserDTO();
        sampleUserDTO.setUserId("user123");
        sampleUserDTO.setFirstName("John");
        sampleUserDTO.setLastName("Smith");
        sampleUserDTO.setEmailAddress("john.smith@example.com");
        sampleUserDTO.setSupervisorUserId("sup1");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateUser() throws Exception {
        Mockito.when(userMapper.toDTO(sampleUser)).thenReturn(sampleUserDTO);
        Mockito.when(userService.createUser(any(UserDTO.class))).thenReturn(sampleUserDTO);


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user123"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testUpdateUser() throws Exception {
        Mockito.when(userMapper.toDTO(sampleUser)).thenReturn(sampleUserDTO);
        Mockito.when(userService.updateUser(eq("user123"), any(UserDTO.class))).thenReturn(sampleUserDTO);


        mockMvc.perform(put("/users/user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"));
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
                .thenReturn(Collections.singletonList(sampleUserDTO));

        mockMvc.perform(get("/users/supervisor/sup1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user123"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testUpdateSupervisor() throws Exception {
        sampleUser.setSupervisorUserId("sup2");
        sampleUserDTO.setSupervisorUserId("sup2");
        Mockito.when(userMapper.toDTO(sampleUser)).thenReturn(sampleUserDTO);
        Mockito.when(userService.updateSupervisor("user123", "sup2")).thenReturn(sampleUserDTO);


        mockMvc.perform(patch("/users/user123/supervisor/sup2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supervisorUserId").value("sup2"));
    }
}
