package com.agency.agency.resource;

import com.agency.agency.domain.model.entity.AgencyServices;
import lombok.*;

import java.util.List;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class AgencyResource {
    private Long id;
    private String name;
    private String email;
    private String password;
    private int phoneNumber;
    private String description;
    private String location;
    private int ruc;
    private String photo;
    private int score;
    private List<AgencyServices> services;
}
