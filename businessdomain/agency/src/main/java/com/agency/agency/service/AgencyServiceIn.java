package com.agency.agency.service;


import com.agency.agency.domain.model.entity.Agency;
import com.agency.agency.domain.model.entity.AgencyServices;
import com.agency.agency.domain.persistence.AgencyRepository;
import com.agency.agency.domain.service.AgencyService;
import com.agency.agency.shared.exception.ResourceNotFoundException;
import com.agency.agency.shared.exception.ResourceValidationException;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class AgencyServiceIn implements AgencyService {
    private static final String ENTITY = "Agency";

    private final AgencyRepository agencyRepository;

    private final Validator validator;

    private final WebClient.Builder webClientBuilder;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;


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

    public AgencyServiceIn(AgencyRepository agencyRepository, Validator validator, WebClient.Builder webClientBuilder) {
        this.agencyRepository = agencyRepository;
        this.validator = validator;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public List<Agency> getAll() {
        List<Agency> agencies = agencyRepository.findAll();

        // this goes ovwer the agencies and their services
        for (Agency agency : agencies) {
            List<AgencyServices> agencyServices = agency.getServices();

            agencyServices.forEach(x-> {
                // in this part we call to microservice service to get the data
                String serviceName=getServiceName(x.getServiceId());
                String serviceDescription=getServiceDescription(x.getServiceId());
                String serviceLocation=getServiceLocation(x.getServiceId());
                Integer serviceScore=getServiceScore(x.getServiceId());
                Float serviceNewPrice= Float.valueOf(getServiceNewPrice(x.getServiceId()));
                Float servicePrice= Float.valueOf(getServicePrice(x.getServiceId()));
                String serviceCreationDate=getServiceCreationDate(x.getServiceId());
                String servicePhotos=getServicePhotos(x.getServiceId());
                Integer serviceIsOffer=getServiceIsOffer(x.getServiceId());
                Integer serviceIsPopular=getServiceIsPopular(x.getServiceId());

                // we update the date of AgencyService
                x.setServiceName(serviceName);
                x.setDescription(serviceDescription);
                x.setLocation(serviceLocation);
                x.setScore(serviceScore);
                x.setNewPrice(serviceNewPrice);
                x.setPrice(servicePrice);
                x.setCreationDate(serviceCreationDate);
                x.setPhotos(servicePhotos);
                x.setIsOffer(serviceIsOffer);
                x.setIsPopular(serviceIsPopular);
            });
        }
        return agencies;
    }

    @Override
    public Page<Agency> getAllPage(Pageable pageable) {
        return agencyRepository.findAll(pageable);
    }

    @Override
    public Agency getByEmail(String email) {

        Agency agency =agencyRepository.findByEmail(email);
        List<AgencyServices> services = agency.getServices();

        services.forEach(x->{
            String serviceName=getServiceName(x.getServiceId());
            String serviceDescription=getServiceDescription(x.getServiceId());
            String serviceLocation=getServiceLocation(x.getServiceId());
            Integer serviceScore=getServiceScore(x.getServiceId());
            Float serviceNewPrice= Float.valueOf(getServiceNewPrice(x.getServiceId()));
            Float servicePrice= Float.valueOf(getServicePrice(x.getServiceId()));
            String serviceCreationDate=getServiceCreationDate(x.getServiceId());
            String servicePhotos=getServicePhotos(x.getServiceId());
            Integer serviceIsOffer=getServiceIsOffer(x.getServiceId());
            Integer serviceIsPopular=getServiceIsPopular(x.getServiceId());

            x.setServiceName(serviceName);
            x.setDescription(serviceDescription);
            x.setLocation(serviceLocation);
            x.setScore(serviceScore);
            x.setNewPrice(serviceNewPrice);
            x.setPrice(servicePrice);
            x.setCreationDate(serviceCreationDate);
            x.setPhotos(servicePhotos);
            x.setIsOffer(serviceIsOffer);
            x.setIsPopular(serviceIsPopular);
        });
        return agency;
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
    public Agency getByLocation(String location) {
        Agency agency=agencyRepository.findByLocation(location);
        List<AgencyServices> services=agency.getServices();

        services.forEach(x->{
            String serviceName=getServiceName(x.getServiceId());
            String serviceDescription=getServiceDescription(x.getServiceId());
            String serviceLocation=getServiceLocation(x.getServiceId());
            Integer serviceScore=getServiceScore(x.getServiceId());
            Float serviceNewPrice= getServiceNewPrice(x.getServiceId());
            Float servicePrice= getServicePrice(x.getServiceId());
            String serviceCreationDate=getServiceCreationDate(x.getServiceId());
            String servicePhotos=getServicePhotos(x.getServiceId());
            Integer serviceIsOffer=getServiceIsOffer(x.getServiceId());
            Integer serviceIsPopular=getServiceIsPopular(x.getServiceId());

            x.setServiceName(serviceName);
            x.setDescription(serviceDescription);
            x.setLocation(serviceLocation);
            x.setScore(serviceScore);
            x.setNewPrice(serviceNewPrice);
            x.setPrice(servicePrice);
            x.setCreationDate(serviceCreationDate);
            x.setPhotos(servicePhotos);
            x.setIsOffer(serviceIsOffer);
            x.setIsPopular(serviceIsPopular);
        });
        return agency;
    }

    @Override
    public Agency create(Agency agency){
        Set<ConstraintViolation<Agency>> violations = validator.validate(agency);

        if(!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        //here we encode the password sdjfhasjfd heeelp i dunno what im doing
        agency.setPassword(passwordEncoder.encode(agency.getPassword()));

        Agency agencyWithEmail = agencyRepository.findByEmail(agency.getEmail());

        if (agencyWithEmail != null)
            throw new ResourceValidationException(ENTITY,
                    "An agency with the same email already exists.");

        Agency agencyWithPhoneNumber = agencyRepository.findByPhoneNumber(agency.getPhoneNumber());

        if (agencyWithPhoneNumber != null)
            throw new ResourceValidationException(ENTITY,
                    "An agency with the same phone number already exists.");

        // Asociar AgencyService con Agency
        List<AgencyServices> services = agency.getServices();
        if (services != null) {
            for (AgencyServices service : services) {
                service.setAgency(agency);
            }
        }

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
                                        .withServices(request.getServices())
                                .withScore(request.getScore())))
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, agencyId));
    }

    @Override
    public Agency getInfoAgencyById(Long agencyId){
        Agency agency = agencyRepository.findById(agencyId).orElseThrow(() ->
                new ResourceNotFoundException(ENTITY, agencyId));
        List<AgencyServices> services = agency.getServices();

        services.forEach(x->{
            String serviceName=getServiceName(x.getServiceId());
            String serviceDescription=getServiceDescription(x.getServiceId());
            String serviceLocation=getServiceLocation(x.getServiceId());
            Integer serviceScore=getServiceScore(x.getServiceId());
            Float serviceNewPrice= getServiceNewPrice(x.getServiceId());
            Float servicePrice= getServicePrice(x.getServiceId());
            String serviceCreationDate=getServiceCreationDate(x.getServiceId());
            String servicePhotos=getServicePhotos(x.getServiceId());
            Integer serviceIsOffer=getServiceIsOffer(x.getServiceId());
            Integer serviceIsPopular=getServiceIsPopular(x.getServiceId());

            x.setServiceName(serviceName);
            x.setDescription(serviceDescription);
            x.setLocation(serviceLocation);
            x.setScore(serviceScore);
            x.setNewPrice(serviceNewPrice);
            x.setPrice(servicePrice);
            x.setCreationDate(serviceCreationDate);
            x.setPhotos(servicePhotos);
            x.setIsOffer(serviceIsOffer);
            x.setIsPopular(serviceIsPopular);
        });
        return agency;
    }

    @Override
    public ResponseEntity<?> delete(Long agencyId) {
        return agencyRepository.findById(agencyId).map(
                agency -> {
                    agencyRepository.delete(agency);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException(ENTITY, agencyId));
    }

    @Override
    public Agency getByName(String name) {
        Agency agency =agencyRepository.findByName(name);
        List<AgencyServices> services = agency.getServices();

        services.forEach(x->{
            String serviceName=getServiceName(x.getServiceId());
            String serviceDescription=getServiceDescription(x.getServiceId());
            String serviceLocation=getServiceLocation(x.getServiceId());
            Integer serviceScore=getServiceScore(x.getServiceId());
            Float serviceNewPrice= getServiceNewPrice(x.getServiceId());
            Float servicePrice= getServicePrice(x.getServiceId());
            String serviceCreationDate=getServiceCreationDate(x.getServiceId());
            String servicePhotos=getServicePhotos(x.getServiceId());
            Integer serviceIsOffer=getServiceIsOffer(x.getServiceId());
            Integer serviceIsPopular=getServiceIsPopular(x.getServiceId());

            x.setServiceName(serviceName);
            x.setDescription(serviceDescription);
            x.setLocation(serviceLocation);
            x.setScore(serviceScore);
            x.setNewPrice(serviceNewPrice);
            x.setPrice(servicePrice);
            x.setCreationDate(serviceCreationDate);
            x.setPhotos(servicePhotos);
            x.setIsOffer(serviceIsOffer);
            x.setIsPopular(serviceIsPopular);
        });
        return agency;
    }
    //JWT AUTHENTICATION
//    @Override
//    public String generateToken(String username) {
//        return jwtService.generateToken(username);
//    }
//
//    @Override
//    public void validateToken(String token) {
//        jwtService.validateToken(token);
//    }
    //END JWT AUTHENTICATION
    @Override
    public String getServiceName(long id) {
        try{
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            String name = block.get("name").asText();
            return name;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getServiceDescription(long id) {
        try{
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            String description = block.get("description").asText();
            return description;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getServiceLocation(long id) {
        try{
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            String location = block.get("location").asText();
            return location;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Integer getServiceScore(long id) {
        try{
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            Integer score = block.get("score").asInt();
            return score;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Float getServicePrice(long id) {

        try{
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            Float price = (float) block.get("price").asInt();
            return price;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Float getServiceNewPrice(long id) {

        try{
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            Float newPrice = (float) block.get("newPrice").asInt();
            return newPrice;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getServiceCreationDate(long id) {
        try{
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            String creationDate = block.get("creationDate").asText();
            return creationDate;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }
    @Override
    public String getServicePhotos(long id) {
        try{
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            String photos = block.get("photos").asText();
            return photos;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Integer getServiceIsOffer(long id) {
        try{
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            Integer isOffer = block.get("isOffer").asInt();
            return isOffer;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Integer getServiceIsPopular(long id) {
        try {
            WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://localhost:8081/api/v1/service/")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                    .build();
            JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                    .retrieve().bodyToMono(JsonNode.class).block();
            Integer isPopular = block.get("isPopular").asInt();
            return isPopular;
        }catch (WebClientResponseException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }
}
