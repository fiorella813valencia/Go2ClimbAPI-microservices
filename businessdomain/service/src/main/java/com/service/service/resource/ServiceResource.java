package com.service.service.resource;

import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResource {
    private Long id;
    private Long serviceId;
    private String name;
    private String description;
    private String location;
    private int score;
    private float price;
    private float newPrice;
    private String creationDate;
    private String photos;
    private int isOffer;
    private int isPopular;
}
