package com.service.service.domain.persistence;

import com.service.service.domain.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findServiceById(Long serviceId);
    Service findByServiceId(Long serviceId);
    Service findByName(String name);
    Service findByLocation(String location);
    Service findByScore(int score);

    Service findByPrice(Float price);



//    @Query("SELECT u FROM Service u WHERE u.isOffer = ?1")
//    List<Service> findByIsOffer(int isOffer);
//
//    @Query("SELECT u FROM Service u WHERE u.isPopular = ?1")
//    List<Service> findByIsPopular(int isPopular);
//
//    @Query("SELECT u FROM Service u WHERE u.name LIKE %?1%")
//    List<Service> findByText(String text);
}
