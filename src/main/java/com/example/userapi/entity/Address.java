package com.example.userapi.entity;


import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mobile_phone", length = 50)
    private String mobilePhone;

    @Column(name = "other_phone", length = 50)
    private String otherPhone;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "street_address", length = 50)
    private String streetAddress;

    @Column(name = "street_address_2", length = 50)
    private String streetAddress2;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 20)
    private String state;

    @Column(name = "postal_code", length = 9)
    private String postalCode;

    @Column(name = "country_code", length = 2)
    private String countryCode = "US";

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

