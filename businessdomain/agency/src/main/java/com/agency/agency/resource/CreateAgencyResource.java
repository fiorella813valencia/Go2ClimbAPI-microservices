package com.agency.agency.resource;

import com.agency.agency.domain.model.entity.AgencyServices;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


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

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "agency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgencyServices> services;
}
