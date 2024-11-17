package com.registrationService.rgService.service;


import com.registrationService.rgService.Utils.CommonUtils;
import com.registrationService.rgService.entities.User;
import com.registrationService.rgService.entities.Login;
import com.registrationService.rgService.entities.UserType;
import com.registrationService.rgService.repositories.LoginRepository;
import com.registrationService.rgService.repositories.UserRepository;
import com.registrationService.rgService.requests.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.registrationService.rgService.Utils.Constants.*;

@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    public LoginRepository loginRepository;

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public CommonUtils commonUtils;


    public LoginRequest saveTheLogin(LoginRequest loginRequest) throws Exception {
        log.info("Login into Service");

        if(loginRequest.getRoleType().equals(UserType.Buyer.name())){
            // check if the SignUpRequired.
            Optional<User> buyerOptional = userRepository.findByEmail(loginRequest.getEmailId());
            if(buyerOptional.isPresent()) {
                if(buyerOptional.get().getHashPassword().equals(commonUtils.getHashPassword(loginRequest.getPassword()))){
                    Login login = new Login();
                    login.setUser(buyerOptional.get());
                    login.setUsername(loginRequest.getEmailId());
                    login.setLastLogin(LocalDateTime.now());
                    login.setPasswordHash(buyerOptional.get().getHashPassword());
                    login.setId(UUID.randomUUID().toString());
                    login.setUserType(UserType.Buyer);
                    loginRepository.save(login);
                    loginRequest.setId(login.getId());
                    return loginRequest;
                }else{
                    throw new Exception(PASSWORD_NOT_MATCH);
                }
            }else {
                throw  new Exception(USER_NOT_REGISTERED);
            }
        }else if (loginRequest.getRoleType().equals(UserType.Seller.name())){
            // check if the SignUpRequired.
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmailId());
            if(userOptional.isPresent()) {
                if(userOptional.get().getHashPassword().equals(commonUtils.getHashPassword(loginRequest.getPassword()))){
                    Login login = new Login();
                    login.setUser(userOptional.get());
                    login.setUsername(loginRequest.getEmailId());
                    login.setLastLogin(LocalDateTime.now());
                    login.setPasswordHash(userOptional.get().getHashPassword());
                    login.setId(UUID.randomUUID().toString());
                    login.setUserType(UserType.Seller);
                    loginRepository.save(login);
                    loginRequest.setId(login.getId());
                    return loginRequest;
                }else{
                    throw new Exception(PASSWORD_NOT_MATCH);
                }
            }else {
                throw  new Exception(USER_NOT_REGISTERED);
            }

        }else{
            throw  new Exception(ROLE_NOT_SPECIFIED);
        }
    }
}
