package com.service.service.resource;

import com.service.service.domain.model.entity.ServiceActivities;
import lombok.*;

import java.util.List;

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
    private Float price;
    private Float newPrice;
    private String creationDate;
    private String photos;
    private int isOffer;
    private int isPopular;
    private List<ServiceActivities> activities;
}
