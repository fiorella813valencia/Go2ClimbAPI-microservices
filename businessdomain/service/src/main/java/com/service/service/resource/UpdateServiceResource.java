package com.service.service.resource;

import com.service.service.domain.model.entity.ServiceActivities;
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
public class UpdateServiceResource {
    private Long id;
    private Long serviceId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String location;
    @NotNull
    private int score;
    @NotNull
    private Float price;
    @NotNull
    private Float newPrice;

    @NotBlank
    private String creationDate;

    @NotBlank
    private String photos;
    @NotNull
    private int isOffer;
    @NotNull
    private int isPopular;

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceActivities> activities;
}
