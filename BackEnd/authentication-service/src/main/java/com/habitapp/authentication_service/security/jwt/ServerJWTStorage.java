package com.menara.authentication.security.jwt;

import com.menara.authentication.annotation.Storage;
import com.menara.authentication.domain.facade.AuthenticationFacade;
import com.menara.authentication.dto.jwt.AccessTokenAndFingerPrintDTO;
import lombok.*;

/**
 * AccessToken is a class for store an access jwt Generated for Authentication-Service
 * And That to authenticate and access to other microservices like User-Service Emailing-service
 */
@Getter
@Setter
@Storage
public class ServerJWTStorage {
    private String accessToken;
    private String fingerprint;

//    public ServerJWTStorage(AuthenticationFacade authenticationFacade){
//        this.authenticationFacade = authenticationFacade;
//        AccessTokenAndFingerPrintDTO accessTokenAndFingerPrintDTO = authenticationFacade.authenticateAuthenticationServiceWithDefaultMethod();
//        System.out.println(accessTokenAndFingerPrintDTO.getAccessToken());
//        System.out.println(accessTokenAndFingerPrintDTO.getFingerPrint());
        //this.accessToken = accessTokenAndFingerPrintDTO.getAccessToken();
        //this.fingerprint = accessTokenAndFingerPrintDTO.getFingerPrint();
//    }
}
