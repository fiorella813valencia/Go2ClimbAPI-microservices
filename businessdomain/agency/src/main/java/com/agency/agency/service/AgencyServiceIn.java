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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;




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

    public AgencyServiceIn(AgencyRepository agencyRepository,
                           Validator validator,
                           WebClient.Builder webClientBuilder,
                           StringRedisTemplate redisTemplate) {
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
                Float serviceNewPrice= getServiceNewPrice(x.getServiceId());
                Float servicePrice= getServicePrice(x.getServiceId());
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
    @Cacheable(value = "serviceNameCache", key = "#id")
    public String getServiceName(long id) {
        String cachedServiceName = (String) redisTemplate.opsForValue().get("serviceName:" + id);
        if (cachedServiceName != null) {
            return cachedServiceName;
        } else {
            try {
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                String name = block.get("name").asText();
                redisTemplate.opsForValue().set("serviceName:" + id, name);
                return name;
            } catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    @Cacheable(value = "serviceDescriptionCache", key = "#id")
    public String getServiceDescription(long id) {
        String cachedServiceDescription=(String) redisTemplate.opsForValue().get("serviceDescription:"+id);
        if(cachedServiceDescription!=null){
            return cachedServiceDescription;
        }else{
            try{
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                String description = block.get("description").asText();
                redisTemplate.opsForValue().set("serviceDescription:"+id,description);
                return description;
            }catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    @Cacheable(value = "serviceLocationCache", key = "#id")
    public String getServiceLocation(long id) {
        String cachedServiceLocation=(String) redisTemplate.opsForValue().get("serviceLocation:"+id);
        if(cachedServiceLocation!=null){
            return cachedServiceLocation;
        }else{
            try{
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                String location = block.get("location").asText();
                redisTemplate.opsForValue().set("serviceLocation:"+id,location);
                return location;
            }catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    @Cacheable(value = "serviceScoreCache", key = "#id")
    public Integer getServiceScore(long id) {
        Integer cachedServiceScore=(Integer) redisTemplate.opsForValue().get("serviceScore:"+id);
        if (cachedServiceScore!=null){
            return cachedServiceScore;
        }else{
            try{
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                Integer score = block.get("score").asInt();
                redisTemplate.opsForValue().set("serviceScore:"+id,score);
                return score;
            }catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }

    }

    @Override
    @Cacheable(value = "servicePriceCache", key = "#id")
    public Float getServicePrice(long id) {
        Float cacheServicePrice=(Float) redisTemplate.opsForValue().get("servicePrice:"+id);
        if(cacheServicePrice!=null){
            return cacheServicePrice;
        }else{
            try{
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                Float price = (float) block.get("price").asInt();
                redisTemplate.opsForValue().set("servicePrice:"+id,price);
                return price;
            }catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    @Cacheable(value = "serviceNewPriceCache", key = "#id")
    public Float getServiceNewPrice(long id) {
        Float cachedServiceNewPrice=(Float) redisTemplate.opsForValue().get("serviceNewPrice:"+id);
        if(cachedServiceNewPrice!=null){
            return cachedServiceNewPrice;
        }else{
            try{
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                Float newPrice = (float) block.get("newPrice").asInt();
                redisTemplate.opsForValue().set("serviceNewPrice:"+id,newPrice);
                return newPrice;
            }catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    @Cacheable(value = "serviceCreationDateCache", key = "#id")
    public String getServiceCreationDate(long id) {
        String cachedServiceCreationDate=(String) redisTemplate.opsForValue().get("serviceCreationDate:"+id);
        if(cachedServiceCreationDate!=null){
            return cachedServiceCreationDate;
        }else{
            try{
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                String creationDate = block.get("creationDate").asText();
                redisTemplate.opsForValue().set("serviceCreationDate:"+id,creationDate);
                return creationDate;
            }catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }
    @Override
    @Cacheable(value = "servicePhotosCache", key = "#id")
    public String getServicePhotos(long id) {
        String cachedServicePhotos=(String) redisTemplate.opsForValue().get("servicePhoto:"+id);
        if(cachedServicePhotos!=null){
            return cachedServicePhotos;
        }else{
            try{
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                String photos = block.get("photos").asText();
                redisTemplate.opsForValue().set("servicePhoto:"+id,photos);
                return photos;
            }catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    @Cacheable(value = "serviceIsOfferCache", key = "#id")
    public Integer getServiceIsOffer(long id) {
        Integer cachedServiceOffer=(Integer) redisTemplate.opsForValue().get("serviceOffer:"+id);
        if(cachedServiceOffer!=null){
            return cachedServiceOffer;
        }else{
            try{
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                Integer isOffer = block.get("isOffer").asInt();
                redisTemplate.opsForValue().set("serviceOffer:"+id,isOffer);
                return isOffer;
            }catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    @Cacheable(value = "serviceIsPopularCache", key = "#id")
    public Integer getServiceIsPopular(long id) {
        Integer cachedServicePopular=(Integer) redisTemplate.opsForValue().get("servicePopular:"+id);
        if(cachedServicePopular!=null){
            return cachedServicePopular;
        }else{
            try {
                WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                        .baseUrl("http://localhost:8081/api/v1/service/")
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/api/v1/service/"))
                        .build();
                JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                        .retrieve().bodyToMono(JsonNode.class).block();
                Integer isPopular = block.get("isPopular").asInt();
                redisTemplate.opsForValue().set("servicePopular:"+id,isPopular);
                return isPopular;
            }catch (WebClientResponseException e) {
                return null;
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
