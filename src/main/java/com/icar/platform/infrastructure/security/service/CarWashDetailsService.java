package com.icar.platform.infrastructure.security.service;

import com.icar.platform.domain.model.carwash.legal.CarWashRegistration;
import com.icar.platform.domain.repository.carwash.legal.CarWashRegistrationDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CarWashDetailsService implements UserDetailsService {

    private final CarWashRegistrationDataRepository carWashRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CarWashRegistration carWash = carWashRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        return new User(carWash.getEmail(), carWash.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CARWASH")));
    }
}