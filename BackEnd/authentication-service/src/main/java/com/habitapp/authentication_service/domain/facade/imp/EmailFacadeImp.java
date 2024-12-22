package com.habitapp.authentication_service.domain.facade.imp;

import com.habitapp.authentication_service.annotation.Facade;
import com.habitapp.authentication_service.domain.facade.EmailFacade;
import com.habitapp.authentication_service.proxy.client.emailing.EmailServiceProxy;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Facade
public class EmailFacadeImp implements EmailFacade {
    private EmailServiceProxy emailService;

}
