package com.agency.agency.domain.model.entity;

import com.agency.agency.shared.domain.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import com.agency.agency.shared.domain.AuditModel;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agencies")
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    @Column(unique = true)
    private String email;
    @NotBlank
    @NotNull
    private String password;
    @NotNull
    @Column(unique = true)
    private int phoneNumber;
    @NotBlank
    @NotNull
    private String description;
    @NotBlank
    @NotNull
    private String location;
    @NotNull
    private int ruc;
    @NotBlank
    @NotNull
    private String photo;
    @NotNull
    private int score;
}
