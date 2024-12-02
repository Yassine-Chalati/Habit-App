package com.habitapp.progress_service.proxy.client.habit;

import com.habitapp.progress_service.annotation.Proxy;
import com.habitapp.progress_service.client.habit.HabitClient;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Proxy
@AllArgsConstructor
@NoArgsConstructor
public class HabitProxy {

    private HabitClient habitClient;

}
