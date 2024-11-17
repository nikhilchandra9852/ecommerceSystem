package com.registrationService.rgService.Utils;


import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Component
public class CommonUtils {


    public String getHashPassword(String password){
        return Arrays.toString(Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8)));
    }


}
