package com.example.userapi.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Integer id;
    private String city;
    private String state;
    private String postalCode;
    private String mobilePhone;
    private String otherPhone;
    private String location;
    private String streetAddress;
    private String streetAddress2;
    private String countryCode ;

}
