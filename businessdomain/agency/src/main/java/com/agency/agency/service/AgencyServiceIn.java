package com.agency.agency.service;


import com.agency.agency.domain.model.entity.Agency;
import com.agency.agency.domain.persistence.AgencyRepository;
import com.agency.agency.domain.service.AgencyService;
import com.agency.agency.shared.exception.ResourceNotFoundException;
import com.agency.agency.shared.exception.ResourceValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AgencyServiceIn implements AgencyService {
    private static final String ENTITY = "Agency";

    private final AgencyRepository agencyRepository;

    private final Validator validator;

    public AgencyServiceIn(AgencyRepository agencyRepository, Validator validator) {
        this.agencyRepository = agencyRepository;
        this.validator = validator;
    }

    @Override
    public List<Agency> getAll() {
        return agencyRepository.findAll();
    }

    @Override
    public Page<Agency> getAll(Pageable pageable) {
        return agencyRepository.findAll(pageable);
    }

    @Override
    public Agency getByEmail(String email) {
        return agencyRepository.findByEmail(email);
    }

    @Override
    public Agency getByPassword(String password) {
        return agencyRepository.findByPassword(password);
    }

    @Override
    public Agency getByEmailAndPassword(String email, String password) {
        return agencyRepository.findByEmailAndPassword(email,password);
    }


    @Override
    public Agency getByName(String name) {
        return agencyRepository.findByName(name);
    }

    @Override
    public Agency getByLocation(String location) {
        return agencyRepository.findByLocation(location);
    }

    @Override
    public Agency create(Agency agency){
        Set<ConstraintViolation<Agency>> violations = validator.validate(agency);

        if(!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        Agency agencyWithEmail = agencyRepository.findByEmail(agency.getEmail());

        if (agencyWithEmail != null)
            throw new ResourceValidationException(ENTITY,
                    "An agency with the same email already exists.");

        Agency agencyWithPhoneNumber = agencyRepository.findByPhoneNumber(agency.getPhoneNumber());

        if (agencyWithPhoneNumber != null)
            throw new ResourceValidationException(ENTITY,
                    "An agency with the same phone number already exists.");

        return agencyRepository.save(agency);
    }
    @Override
    public Agency update(Long agencyId, Agency request){
        Set<ConstraintViolation<Agency>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        return agencyRepository.findById(agencyId).map(agency ->
                        agencyRepository.save(agency.withName(request.getName())
                                .withEmail(request.getEmail())
                                .withPassword(request.getPassword())
                                .withPhoneNumber(request.getPhoneNumber())
                                .withDescription(request.getDescription())
                                .withLocation(request.getLocation())
                                .withRuc(request.getRuc())
                                .withPhoto(request.getPhoto())
                                .withScore(request.getScore())))
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, agencyId));
    }

    @Override
    public Agency getInfoAgencyById(Long agencyId){
        return agencyRepository.findById(agencyId).orElseThrow(() ->
                new ResourceNotFoundException(ENTITY, agencyId));
    }

    @Override
    public ResponseEntity<?> delete(Long agencyId) {
        return agencyRepository.findById(agencyId).map(
                agency -> {
                    agencyRepository.delete(agency);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException(ENTITY, agencyId));
    }
}
