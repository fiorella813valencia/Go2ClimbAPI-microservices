package com.agency.agency.resource;

import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuthentication {
    private String username;
    private String password;
}
