package com.agency.agency.resource;

import lombok.*;

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
}
