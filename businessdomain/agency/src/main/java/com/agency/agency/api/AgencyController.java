package com.agency.agency.api;

import com.agency.agency.domain.service.AgencyService;
import com.agency.agency.mapping.AgencyMapper;
import com.agency.agency.resource.AgencyResource;
import com.agency.agency.resource.CreateAgencyResource;
import com.agency.agency.resource.UpdateAgencyResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "api/v1/agencies")
public class AgencyController {
    private final AgencyService agencyService;
    private final AgencyMapper mapper;

    public AgencyController(AgencyService agencyService, AgencyMapper mapper) {
        this.agencyService = agencyService;
        this.mapper = mapper;
    }

    //funciona GET ALL
    @GetMapping
    public Page<AgencyResource> getAllAgencies(Pageable pageable)
    {
        return mapper.modelListPage(agencyService.getAll(), pageable);
    }

    //funciona GET BY ID
    @GetMapping("{agencyId}")
    public AgencyResource getInfoAgencyById(@PathVariable Long agencyId) {
        return mapper.toResource(agencyService.getInfoAgencyById(agencyId));
    }
    //funciona GET BY NAME
    @GetMapping("name/{agencyName}")
    public AgencyResource getInfoAgencyByName(@PathVariable("agencyName") String agencyName) {
        return mapper.toResource(agencyService.getByName(agencyName));
    }

    // funciona GET BY EMAIL
    @GetMapping("email/{agencyEmail}")
    public AgencyResource getInfoAgencyByEmail(@PathVariable("agencyEmail") String agencyEmail) {
        return mapper.toResource(agencyService.getByEmail(agencyEmail));
    }

    // funciona GET BY PASSWORD AND EMAIL
    @GetMapping("email&password/{agencyEmail}/{agencyPassword}")
    public AgencyResource getInfoAgencyByEmailAndPassword(@PathVariable("agencyEmail") String agencyEmail, @PathVariable("agencyPassword") String agencyPassword) {
        return mapper.toResource(agencyService.getByEmailAndPassword(agencyEmail,agencyPassword));
    }

    // funciona GET BY LOCATION
    @GetMapping("location/{agencyLocation}")
    public AgencyResource getInfoAgencyByLocation(@PathVariable("agencyLocation") String agencyLocation) {
        return mapper.toResource(agencyService.getByLocation(agencyLocation));
    }




    //funciona POST
    @PostMapping
    public AgencyResource createAgency(@RequestBody CreateAgencyResource resource){
        return mapper.toResource(agencyService.create(mapper.toModel(resource)));
    }

    //funciona UPDATE
    @PutMapping("/{agencyId}")
    public AgencyResource updateAgency(@PathVariable Long agencyId, @RequestBody UpdateAgencyResource resource) {
        return mapper.toResource(agencyService.update(agencyId, mapper.toModel(resource)));
    }

    //funciona DELETE
    @DeleteMapping("{agencyId}")
    public ResponseEntity<?> deleteAgency(@PathVariable Long agencyId) {
        return agencyService.delete(agencyId);
    }
}
