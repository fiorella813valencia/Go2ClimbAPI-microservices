package com.service.service.domain.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


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
    private Float price;

    @NotNull
    private Float newPrice;

    @NotNull
    @NotBlank
    private String creationDate;

    @NotNull
    @NotBlank
    private String photos;

    @NotNull
    private Integer isOffer;

    @NotNull
    private Integer isPopular;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceActivities> activities;

    //Relationships
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "agency_id", nullable = false)
//    private Agency agency;

}
