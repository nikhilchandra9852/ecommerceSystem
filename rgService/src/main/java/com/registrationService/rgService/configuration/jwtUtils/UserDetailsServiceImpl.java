package com.registrationService.rgService.configuration.jwtUtils;

import com.registrationService.rgService.entities.User;
import com.registrationService.rgService.repositories.UserRepository;
import com.registrationService.rgService.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // find the user from
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User is not Found Exception" + email));
        return UserService.build(seller);
    }
}
