package com.habitapp.habit_service.proxy.client.notification;

import com.habitapp.habit_service.annotation.Proxy;
import com.habitapp.habit_service.client.notification.NotificationClient;

import lombok.AllArgsConstructor;

@Proxy
@AllArgsConstructor
public class NotificationProxy {
    private NotificationClient notificationClient;
    
}
