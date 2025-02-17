package com.motorny.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "name")
    private String name;

    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(min = 6, max = 120)
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @OneToMany(mappedBy = "senderId")
    private List<Shipment> shipments;

    @OneToMany(mappedBy = "userId")
    private List<Feedback> feedbacks;

}
