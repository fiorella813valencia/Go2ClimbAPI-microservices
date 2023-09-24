package com.service.service.domain.persistence;

import com.service.service.domain.model.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findServiceById(Long serviceId);
    Service findByServiceId(Long serviceId);


//    @Query("SELECT u FROM Service u WHERE u.isOffer = ?1")
//    List<Service> findByIsOffer(int isOffer);
//
//    @Query("SELECT u FROM Service u WHERE u.isPopular = ?1")
//    List<Service> findByIsPopular(int isPopular);
//
//    @Query("SELECT u FROM Service u WHERE u.name LIKE %?1%")
//    List<Service> findByText(String text);
}
