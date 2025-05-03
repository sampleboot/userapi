package com.example.userapi.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddressDTO {
    private Integer id;

    @Size(max = 50)
    private String city;

    @Size(max = 20)
    private String state;

    @Size(max = 9)
    private String postalCode;

    private String mobilePhone;

    private String otherPhone;

    private String location;

    @Size(max = 50)
    private String streetAddress;

    @Size(max = 50)
    private String streetAddress2;

    @Size(max = 2)
    private String countryCode ;

   // private String userId;

}
