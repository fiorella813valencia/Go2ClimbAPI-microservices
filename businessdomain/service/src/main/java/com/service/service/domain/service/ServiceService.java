package com.service.service.domain.service;

import com.service.service.domain.model.entity.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ServiceService {

    List<Service> getAll();

    Service getServiceById(Long Id);
    Service getServiceByServiceId(Long serviceId);
    Service getServiceByLocation(String location);
    Service getServiceByPrice(Float price);

    Service getServiceByName(String name);

//    List<Service> getAllByAgencyId(Long agencyId);
//
//    List<Service> getAllByIsOffer(int isOffer);
//
//    List<Service> getAllByIsPopular(int isPopular);
//
//    List<Service> getAllByText(String text);
//
//    Page<Service> getAllByAgencyId(Long agencyId, Pageable pageable);


    Service create(Service service);

    Service update(Long serviceId, Service service);

    ResponseEntity<?> delete(Long Id);

    //API CONNECTION
    String getActivityName(long id);
    String getActivityDescription(long id);


}
