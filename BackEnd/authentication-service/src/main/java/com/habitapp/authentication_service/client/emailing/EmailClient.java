package com.habitapp.authentication_service.client.emailing;

import com.internship_hiring_menara.common.http.response.email.EmailAndUrlResponseHttp;
import com.menara.authentication.configuration.client.EmailingConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "emailing-service", configuration = EmailingConfiguration.class)
public interface EmailClient {
    @PostMapping("/email/send-url-activation-account")
    public ResponseEntity<?> sendURLActivationAccount(@RequestBody EmailAndUrlResponseHttp emailAndURL);

    @PostMapping("/email/send-url-reset-password-account")
    public ResponseEntity<?> sendURLResetPassword(@RequestBody EmailAndUrlResponseHttp emailAndURL);

    @PostMapping("/email/send-information-of-password-reset")
    public ResponseEntity<?> sendInformationOfPasswordReset(@RequestBody String email);
}
