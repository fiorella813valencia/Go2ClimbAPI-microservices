package com.agency.agency.api;

import com.agency.agency.domain.service.AgencyService;
import com.agency.agency.mapping.AgencyMapper;
import com.agency.agency.resource.AgencyResource;
import com.agency.agency.resource.CreateAgencyResource;
import com.agency.agency.resource.CreateAuthentication;
import com.agency.agency.service.JWTService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "auth")
public class AuthController {
    private final AgencyService agencyService;
    private final AgencyMapper mapper;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthController(AgencyService agencyService, AgencyMapper mapper) {
        this.agencyService = agencyService;
        this.mapper = mapper;

    }

    //funciona POST
    @Transactional
    @PostMapping("/register")
    public AgencyResource createAgency(@RequestBody CreateAgencyResource resource){
        return mapper.toResource(agencyService.create(mapper.toModel(resource)));
    }
    //JWT IMPLEMENTATION
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody CreateAuthentication authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        System.out.println("Enter?");
        if (authentication.isAuthenticated()) {
            System.out.println("no way?");
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }


    }


}
