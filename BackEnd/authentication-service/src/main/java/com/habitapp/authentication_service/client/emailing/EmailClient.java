package com.habitapp.authentication_service.client.emailing;

import com.habitapp.common.http.response.email.EmailAndUrlResponseHttp;
import com.habitapp.authentication_service.configuration.client.EmailingConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "emailing-service", configuration = EmailingConfiguration.class)
public interface EmailClient {
    @PostMapping("/email/send-url-activation-account")
    public ResponseEntity<?> sendURLActivationAccount(@RequestBody EmailAndUrlResponseHttp emailAndURL);
}
