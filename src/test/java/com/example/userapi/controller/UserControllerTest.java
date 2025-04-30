package com.example.userapi.controller;

import com.example.userapi.entity.User;
import com.example.userapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.MockServiceConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User sampleUser;

    @BeforeEach
    public void setUp() {
        sampleUser = new User();
        sampleUser.setUserId("u123");
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
        sampleUser.setEmailAddress("john.doe@example.com");
    }

    @Test
    public void testCreateUser() throws Exception {
        Mockito.when(userService.createUser(any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("u123"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        Mockito.when(userService.updateUser(eq("u123"), any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(put("/users/u123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("u123"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("u123");

        mockMvc.perform(delete("/users/u123"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetUsersBySupervisor() throws Exception {
        Mockito.when(userService.getUsersBySupervisor("sup1")).thenReturn(Collections.singletonList(sampleUser));

        mockMvc.perform(get("/users/supervisor/sup1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("u123"));
    }

    @Test
    public void testUpdateSupervisor() throws Exception {
        sampleUser.setSupervisorUserId("sup1");
        Mockito.when(userService.updateSupervisor("u123", "sup1")).thenReturn(sampleUser);

        mockMvc.perform(put("/users/u123/supervisor/sup1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supervisorUserId").value("sup1"));
    }

    @TestConfiguration
    static class MockServiceConfig {

        @Bean
        @Primary
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }
}
