package com.agency.agency.api;

import com.agency.agency.domain.service.AgencyService;
import com.agency.agency.mapping.AgencyMapper;
import com.agency.agency.resource.AgencyResource;
import com.agency.agency.resource.UpdateAgencyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping(value = "api/v1/agencies")
public class AgencyController {
    private final AgencyService agencyService;
    private final AgencyMapper mapper;
    @Autowired
    private AuthenticationManager authenticationManager;

//    private final WebClient.Builder webClientBuilder;
//
//
//    //webClient requires HttpClient library to work propertly
//    HttpClient client = HttpClient.create()
//            //Connection Timeout: is a period within which a connection between a client and a server must be established
//            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//            .option(ChannelOption.SO_KEEPALIVE, true)
//            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
//            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
//            //Response Timeout: The maximun time we wait to receive a response after sending a request
//            .responseTimeout(Duration.ofSeconds(1))
//            // Read and Write Timeout: A read timeout occurs when no data was read within a certain
//            //period of time, while the write timeout when a write operation cannot finish at a specific time
//            .doOnConnected(connection -> {
//                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
//                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
//            });

    public AgencyController(AgencyService agencyService, AgencyMapper mapper) {
        this.agencyService = agencyService;
        this.mapper = mapper;

    }

    //funciona GET ALL
    @GetMapping("all")
    public List<AgencyResource> getAllAgencies()
    {
        return mapper.modelList(agencyService.getAll());
    }

    //funciona GET BY ID
    @GetMapping("id/{agencyId}")
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
        System.out.println("HOLAA COMO ESTASSSSSSSSSSSSSSSSS");
        return mapper.toResource(agencyService.getByLocation(agencyLocation));
    }






    //funciona UPDATE
    @PutMapping("update/{agencyId}")
    public AgencyResource updateAgency(@PathVariable Long agencyId, @RequestBody UpdateAgencyResource resource) {
        return mapper.toResource(agencyService.update(agencyId, mapper.toModel(resource)));
    }

    //funciona DELETE
    @DeleteMapping("delete{agencyId}")
    public ResponseEntity<?> deleteAgency(@PathVariable Long agencyId) {
        return agencyService.delete(agencyId);
    }



}
