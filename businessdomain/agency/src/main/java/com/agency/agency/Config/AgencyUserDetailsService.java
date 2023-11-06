package com.agency.agency.Config;

import com.agency.agency.domain.model.entity.Agency;
import com.agency.agency.domain.persistence.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class AgencyUserDetailsService implements UserDetailsService {
    @Autowired
    private AgencyRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Agency> credential= Optional.ofNullable(repository.findByName(username));
        return credential.map(AgencyUserDetails::new).orElseThrow(()->new UsernameNotFoundException("user not found with the name: "+ username));
    }
}
