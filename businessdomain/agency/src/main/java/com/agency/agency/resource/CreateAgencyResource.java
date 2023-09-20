package com.agency.agency.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;



@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreateAgencyResource {
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String password;
    @NotNull
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
