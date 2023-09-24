package com.service.service.service;

import com.service.service.domain.model.entity.Service;
import com.service.service.domain.persistence.ServiceRepository;
import com.service.service.domain.service.ServiceService;
import com.service.service.shared.exception.ResourceNotFoundException;
import com.service.service.shared.exception.ResourceValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private static final String ENTITY = "Service";

    private final ServiceRepository serviceRepository;


    private final Validator validator;

    public ServiceServiceImpl(ServiceRepository serviceRepository, Validator validator) {
        this.serviceRepository = serviceRepository;
        this.validator = validator;
    }


    @Override
    public List<Service> getAll() {
        return serviceRepository.findAll();
    }

    @Override
    public Service getServiceById(Long Id) {
        return serviceRepository.findById(Id).orElseThrow(() ->
                new ResourceNotFoundException(ENTITY, Id));
    }

    @Override
    public Service getServiceByServiceId(Long serviceId) {
        return serviceRepository.findByServiceId(serviceId);
    }


//    @Override
//    public List<Service> getAllByAgencyId(Long agencyId) {
//        return serviceRepository.findByAgencyId(agencyId);
//    }
//
//    @Override
//    public List<Service> getAllByIsOffer(int isOffer) {
//        return serviceRepository.findByIsOffer(isOffer);
//    }
//
//    @Override
//    public List<Service> getAllByIsPopular(int isPopular) {
//        return serviceRepository.findByIsPopular(isPopular);
//    }
//
//    @Override
//    public List<Service> getAllByText(String text) {
//        return serviceRepository.findByText(text);
//    }

//    @Override
//    public Page<Service> getAllByAgencyId(Long agencyId, Pageable pageable) {
//        return serviceRepository.findByAgencyId(agencyId, pageable);
//    }

    @Override
    public Service create(Service service) {
        Set<ConstraintViolation<Service>> violations = validator.validate(service);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        return serviceRepository.save(service);
    }

    @Override
    public Service update(Long serviceId, Service service) {
        Set<ConstraintViolation<Service>> violations = validator.validate(service);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);


        return serviceRepository.findById(serviceId).map(existingService ->
                serviceRepository.save(existingService.withName(service.getName())
                        .withDescription(service.getDescription())
                        .withServiceId(service.getServiceId())
                        .withLocation(service.getLocation())
                        .withScore(service.getScore())
                        .withPrice(service.getPrice())
                        .withNewPrice(service.getNewPrice())
                        .withPhotos(service.getPhotos())
                        .withIsOffer(service.getIsOffer())
                        .withIsPopular(service.getIsPopular())
                        .withCreationDate(service.getCreationDate())))
                .orElseThrow(() -> new ResourceNotFoundException("Service", serviceId));
    }

    @Override
    public ResponseEntity<?> delete( Long serviceId) {
        return serviceRepository.findById(serviceId).map(
                service -> {
                    serviceRepository.delete(service);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException(ENTITY, serviceId));
    }
}
