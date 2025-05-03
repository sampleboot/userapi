package com.example.userapi.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.*;


@Data
public class UserDTO {
    private Integer id;

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

    private Integer createUserId;

    private LocalDateTime createDttm;

    private Integer updateUserId;

    private LocalDateTime updateDttm;

    private List<AddressDTO> addresses;
}

