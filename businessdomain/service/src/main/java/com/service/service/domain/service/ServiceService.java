package com.service.service.domain.service;

import com.service.service.domain.model.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ServiceService {

    List<Service> getAll();

    Service getServiceById(Long Id);
    Service getServiceByServiceId(Long serviceId);

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

}
