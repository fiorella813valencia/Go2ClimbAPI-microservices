package com.service.service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.service.service.domain.model.entity.Service;
import com.service.service.domain.model.entity.ServiceActivities;
import com.service.service.domain.persistence.ServiceRepository;
import com.service.service.domain.service.ServiceService;
import com.service.service.shared.exception.ResourceNotFoundException;
import com.service.service.shared.exception.ResourceValidationException;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private static final String ENTITY = "Service";
    private final ServiceRepository serviceRepository;
    private final Validator validator;
    private final WebClient.Builder webClientBuilder;


    //webClient requires HttpClient library to work propertly
    HttpClient client = HttpClient.create()
            //Connection Timeout: is a period within which a connection between a client and a server must be established
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //Response Timeout: The maximun time we wait to receive a response after sending a request
            .responseTimeout(Duration.ofSeconds(1))
            // Read and Write Timeout: A read timeout occurs when no data was read within a certain
            //period of time, while the write timeout when a write operation cannot finish at a specific time
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    public ServiceServiceImpl(ServiceRepository serviceRepository, Validator validator, WebClient.Builder webClientBuilder) {
        this.serviceRepository = serviceRepository;
        this.validator = validator;
        this.webClientBuilder = webClientBuilder;
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

    @Override
    public Service getServiceByLocation(String location) {
        return serviceRepository.findByLocation(location);
    }

    @Override
    public Service getServiceByPrice(float price) {
        Service service =serviceRepository.findByPrice(price);
        List<ServiceActivities> activities = service.getActivities();

        activities.forEach(x->{
            String serviceName=getActivityName(x.getActivityId());
            String serviceDescription=getActivityDescription(x.getActivityId());

            x.setName(serviceName);
            x.setDescription(serviceDescription);
        });

        return service;
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

    @Override
    public String getActivityName(long id) {
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8084/api/activities/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8084/api/activities/"))
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        String name = block.get("name").asText();
        return name;
    }

    @Override
    public String getActivityDescription(long id) {
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8084/api/activities/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8084/api/activities/"))
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        String description = block.get("description").asText();
        return description;
    }
}
