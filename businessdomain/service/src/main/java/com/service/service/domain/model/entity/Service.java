package com.service.service.domain.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;



@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long serviceId;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    private String location;

    @NotNull
    private int score;

    @NotNull
    private float price;

    @NotNull
    private float newPrice;

    @NotNull
    @NotBlank
    private String creationDate;

    @NotNull
    @NotBlank
    private String photos;

    @NotNull
    private int isOffer;

    @NotNull
    private int isPopular;


    //Relationships
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "agency_id", nullable = false)
//    private Agency agency;

}
