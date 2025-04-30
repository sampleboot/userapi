package com.example.userapi.controller;

import com.example.userapi.entity.User;
import com.example.userapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
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
        sampleUser.setUserId("user123");
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Smith");
        sampleUser.setEmailAddress("john.smith@example.com");
        sampleUser.setSupervisorUserId("sup1");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateUser() throws Exception {
        Mockito.when(userService.createUser(any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/users")
                        .with(httpBasic("admin", "adminpassword"))  // ✅ Add basic auth
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testUpdateUser() throws Exception {
        Mockito.when(userService.updateUser(eq("user23"), any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(put("/users/user123").with(httpBasic("admin", "adminpassword"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("user123");
        mockMvc.perform(delete("/users/user123") .with(httpBasic("admin", "adminpassword")))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testGetUsersBySupervisor() throws Exception {
        Mockito.when(userService.getUsersBySupervisor("sup1")).thenReturn(Collections.singletonList(sampleUser));

        mockMvc.perform(get("/users/supervisor/sup1").with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user123"));
    }

    @Test
    public void testUpdateSupervisor() throws Exception {
        sampleUser.setSupervisorUserId("sup2");
        Mockito.when(userService.updateSupervisor("user123", "sup2")).thenReturn(sampleUser);

        mockMvc.perform(put("/users/user123/supervisor/sup2").with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supervisorUserId").value("sup2"));
    }


}
