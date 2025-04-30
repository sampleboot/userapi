package com.example.userapi.controller;

import com.example.userapi.entity.User;
import com.example.userapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUsersBySupervisor() throws Exception {
        User user = new User();
        user.setUserId("john123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setSupervisorUserId("super123");

        Mockito.when(userService.getUsersBySupervisor("super123"))
                .thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/users/supervisor/super123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("john123"));
    }

    @Test
    public void testUpdateSupervisor() throws Exception {
        User user = new User();
        user.setUserId("john123");
        user.setSupervisorUserId("super456");

        Mockito.when(userService.updateSupervisor("john123", "super456"))
                .thenReturn(user);

        mockMvc.perform(patch("/api/users/john123/supervisor")
                        .param("supervisorUserId", "super456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supervisorUserId").value("super456"));
    }
}
