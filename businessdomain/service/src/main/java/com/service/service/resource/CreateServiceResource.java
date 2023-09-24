package com.service.service.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceResource {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String location;
    @NotNull
    private int score;
    @NotNull
    private float price;
    @NotNull
    private float newPrice;

    @NotBlank
    private String creationDate;

    @NotBlank
    private String photos;
    @NotNull
    private int isOffer;
    @NotNull
    private int isPopular;
}
