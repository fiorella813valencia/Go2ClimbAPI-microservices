package com.agency.agency.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AgencyServices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long serviceId;
    @Transient
    private String serviceName;
    private String description;
    private String location;
    private Integer score;
    private Float price;
    private Float newPrice;
    private String creationDate;
    private String photos;
    private Integer isOffer;
    private Integer isPopular;


//    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Agency.class) ///
//    @JoinColumn(name = "agencyId",nullable = true)
//    private Agency agency;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id") // Cambiado de agencyId a agency_id
    private Agency agency;

}
