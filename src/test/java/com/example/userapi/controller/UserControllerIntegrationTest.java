package com.example.userapi.controller;



import com.example.userapi.dto.AddressDTO;

import com.example.userapi.dto.UserRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class UserControllerIntegrationTest {


    @Value("${local.server.port}")
    private int port;

    private UserRequest createUserDTO(String userId) {

        UserRequest sampleUserDTO = new UserRequest();
        sampleUserDTO.setUserId(userId);
        sampleUserDTO.setFirstName("John" + userId);
        sampleUserDTO.setLastName("Smith" + userId);
        sampleUserDTO.setEmailAddress("john.smith@example.com");
        sampleUserDTO.setSupervisorUserId("sup1");

        sampleUserDTO.setUpdateDttm(LocalDateTime.now());
        sampleUserDTO.setCreateUserId("testuser");

        AddressDTO address = new AddressDTO();
        address.setCity("Ashburn");
        address.setCountryCode("US");
        address.setLocation("location");
        address.setStreetAddress("Address" + userId);
        List<AddressDTO> addresses = new ArrayList<AddressDTO>();
        addresses.add(address);
        sampleUserDTO.setAddresses(addresses);
        return sampleUserDTO;

    }


    private String createUserAndGetId(String userId) {
        var newUser = createUserDTO(userId);
        return given()
                .contentType(ContentType.JSON)
                .auth().basic("admin", "adminpassword")
                .body(newUser)
                .port(port)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .path("userId");
    }

    @Test
    void shouldCreateUser() {
        var newUser = createUserDTO("Test_User");

        given()
                .contentType(ContentType.JSON)
                .auth().basic("admin", "adminpassword")
                .body(newUser)
                .port(port)
                .when()
                .post("/users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("userId", equalTo("Test_User"));
    }

    @Test
    void shouldDeleteUserById() {
        String userId = createUserAndGetId("Test_user11");

        given()
                .port(port)
                .auth().basic("admin", "adminpassword")
                .when()
                .delete("/users/{id}", userId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .body(emptyOrNullString());
    }



    @Test
    void shouldGetUserById() {
        String userId = createUserAndGetId("Test_user567");

        given()
                .port(port)
                .auth().basic("user", "password")
                .when()
                .get("/users/{id}", userId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("userId", equalTo(userId));


    }

    @Test
    void shouldReturn404WhenSearchForNonExistentUser() {
        String nonExistentUserId = "9999";

        given()
                .port(port)
                .auth().basic("user", "password")
                .when()
                .get("/users/{id}", nonExistentUserId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentUser() {
        String nonExistentUserId = "9999";

        given()
                .port(port)
                .auth().basic("admin", "adminpassword")
                .when()
                .delete("/users/{id}", nonExistentUserId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}