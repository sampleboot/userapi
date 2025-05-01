package com.example.userapi.entity;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", unique = true,nullable = false, length = 30)
    private String userId;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "email_address", nullable = false, length = 255)
    private String emailAddress;

    @Column(name = "supervisor_user_id", length = 30)
    private String supervisorUserId;

    @Column(name = "title_text", length = 100)
    private String titleText;

    @Column(name = "create_user_id", nullable = false)
    private Integer createUserId;

    @Column(name = "create_dttm", nullable = false)
    private LocalDateTime createDttm = LocalDateTime.now();

    @Column(name = "update_user_id", nullable = false)
    private Integer updateUserId;

    @Column(name = "update_dttm", nullable = false)
    private LocalDateTime updateDttm = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     private List<Address> addresses;

}

