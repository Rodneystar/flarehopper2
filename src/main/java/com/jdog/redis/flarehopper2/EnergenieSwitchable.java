package com.jdog.redis.flarehopper2;

import com.jdog.redis.flarehopper2.dailytimer.Switchable;

public class EnergenieSwitchable implements Switchable {


    @Override
    public void on() {
        System.out.println("energenie on");
    }

    @Override
    public void off() {
        System.out.println("energenie off");
    }
}
