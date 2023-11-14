package com.service.service.api;

import com.service.service.domain.service.ServiceService;
import com.service.service.mapping.ServiceMapper;
import com.service.service.resource.CreateServiceResource;
import com.service.service.resource.ServiceResource;
import com.service.service.resource.UpdateServiceResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@Tag(name = "Service", description = "Read services")
@RestController
@RequestMapping("api/v1/service")
public class ServiceController {
    private final ServiceService serviceService;
    private final ServiceMapper mapper;

    public ServiceController(ServiceService serviceService, ServiceMapper mapper) {
        this.serviceService = serviceService;
        this.mapper = mapper;
    }

    //funciona
    @Operation(summary = "Get All services", description = "Get all services stored in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResource.class))})
    })
    @GetMapping
    public List<ServiceResource> getAllServices() {
        return mapper.modelList(serviceService.getAll());
    }

//    @Operation(summary = "Get All services filtered by offer", description = "Get All services filtered by offer stored in the database.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Services found",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ServiceResource.class))})
//    })
//    @GetMapping("isOffer={isOffer}")
//    public Page<ServiceResource> getAllByIsOffer(@PathVariable int isOffer, Pageable pageable) {
//        System.out.print("s");
//        return mapper.modelListPage(serviceService.getAllByIsOffer(isOffer), pageable);
//    }
//
//    @Operation(summary = "Get All services filtered by popular", description = "Get All services filtered by popular stored in the database.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Services found",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ServiceResource.class))})
//    })
//    @GetMapping("isPopular={isPopular}")
//    public Page<ServiceResource> getAllByIsPopular(@PathVariable int isPopular, Pageable pageable) {
//        return mapper.modelListPage(serviceService.getAllByIsPopular(isPopular), pageable);
//    }

//    @Operation(summary = "Get All services filtered by text", description = "Get All services filtered by text stored in the database.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Services found",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ServiceResource.class))})
//    })
//    @GetMapping("name_like={text}")
//    public Page<ServiceResource> getAllByText(@PathVariable String text, Pageable pageable) {
//        return mapper.modelListPage(serviceService.getAllByText(text), pageable);
//    }

    //funciona
    @Operation(summary = "Get All services by serviceId", description = "Get All services by serviceId stored in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResource.class))})
    })
    @GetMapping("{serviceId}")
    public ServiceResource getById (@PathVariable Long serviceId) {
        return mapper.toResource(serviceService.getServiceByServiceId(serviceId));
    }

    //funciona NAME
    @Operation(summary = "Get All services by name", description = "Get All services by name stored in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResource.class))})
    })
    @GetMapping("/name/{name}")
    public ServiceResource getByName (@PathVariable String name) {
        return mapper.toResource(serviceService.getServiceByName(name));
    }

    //funciona LOCATION
    @Operation(summary = "Get All services by location", description = "Get All services by location stored in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResource.class))})
    })
    @GetMapping("/location/{location}")
    public ServiceResource getByLocation (@PathVariable String location) {
        return mapper.toResource(serviceService.getServiceByLocation(location));
    }

    @Operation(summary = "Get All services by price", description = "Get All services by price stored in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Services found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResource.class))})
    })
    @GetMapping("/price/{price}")
    public ServiceResource getByPrice (@PathVariable float price) {
        return mapper.toResource(serviceService.getServiceByPrice(price));
    }



    @Operation(summary = "Create a service", description = "Create a service by agencyId in database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResource.class))})
    })
    @Transactional
    @PostMapping
    public ServiceResource createService(@RequestBody CreateServiceResource resource) {
        return mapper.toResource(serviceService.create(mapper.toModel(resource)));
    }

    //funciona
    @Operation(summary = "Update a service", description = "Update a service in database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResource.class))})
    })
    @PutMapping("{serviceId}")
    public ServiceResource updateService(@PathVariable Long serviceId,
                                         @RequestBody UpdateServiceResource resource) {
        return mapper.toResource(serviceService.update(serviceId, mapper.toModel(resource)));
    }

    //funciona
    @Operation(summary = "Delete a service", description = "Delete a service from database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service deleted", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("{serviceId}")
    public ResponseEntity<?> deleteService(@PathVariable Long serviceId) {
        return serviceService.delete(serviceId);
    }
}
