package com.example.userapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserRequest {

    @NotBlank
    @Size(max = 30)
    private String userId;

    @NotBlank(message = "Last Name is required")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "First Name is required")
    @Size(max = 100)
    private String firstName;

    @Email(message = "Email should be valid")
    private String emailAddress;

    @Size(max = 30)
    private String supervisorUserId;

    @Size(max = 100)
    private String titleText;

    @NotBlank
    @Size(max = 30)
    private String createUserId;
    private LocalDateTime updateDttm;
    private List<AddressDTO> addresses;
}