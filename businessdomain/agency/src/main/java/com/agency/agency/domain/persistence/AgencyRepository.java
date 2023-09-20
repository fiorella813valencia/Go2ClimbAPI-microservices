package com.agency.agency.domain.persistence;

import com.agency.agency.domain.model.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Agency findByEmail(String email);
    Agency findByPhoneNumber(int phoneNumber);
    Agency findByName(String name);
    Agency findByEmailAndPassword(String email, String password);
    Agency findByPassword(String password);
    Agency findByLocation(String location);
}
