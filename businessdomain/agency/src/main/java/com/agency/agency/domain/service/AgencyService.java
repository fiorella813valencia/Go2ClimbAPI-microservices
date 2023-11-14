package com.agency.agency.domain.service;

import com.agency.agency.domain.model.entity.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface  AgencyService {

    List<Agency> getAll();

    Page<Agency> getAllPage(Pageable pageable);

    //GET BY EMAIL
    Agency getByEmail(String email);

    //GET BY password
    Agency getByPassword(String name);

    Agency getByEmailAndPassword(String email,String password);


    //GET BY LOCATION
    Agency getByLocation(String location);

    // Create a new agency
    Agency create(Agency agency);

    // Update agency
    Agency update(Long agencyId, Agency agency);

    // Get info agency by id
    Agency getInfoAgencyById(Long agencyId);

    ResponseEntity<?> delete(Long agencyId);

    //API CONNECTION
    String getServiceName(long id);
    String getServiceDescription(long id);
    String getServiceLocation(long id);
    Integer getServiceScore(long id);
    Float getServiceNewPrice(long id);
    Float getServicePrice(long id);
    String getServiceCreationDate(long id);
    String getServicePhotos(long id);
    Integer getServiceIsOffer(long id);
    Integer getServiceIsPopular(long id);

    //GET BY NAME
    Agency getByName(String name);

    //JWT SERVICES
//    public String generateToken(String username);
//    public void validateToken(String token);
}
