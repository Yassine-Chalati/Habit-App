package com.habitapp.notification_service.proxy.client.servicePrefixName;


import com.habitapp.notification_service.annotation.Proxy;
import com.habitapp.notification_service.client.ServicePrefixName1.ServicePrefixName1Client;

import lombok.AllArgsConstructor;

@Proxy
@AllArgsConstructor
public class ProxyNameProxy {
    private ServicePrefixName1Client servicePrefixName1Client;


}
