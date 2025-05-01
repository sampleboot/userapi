package com.example.userapi.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {
    private Integer id;
    private String userId;
    private String lastName;
    private String firstName;
    private String emailAddress;
    private String supervisorUserId;
    private String titleText;
    private Integer createUserId;
    private LocalDateTime createDttm;
    private Integer updateUserId;
    private LocalDateTime updateDttm;
    private List<AddressDTO> addresses;
}

