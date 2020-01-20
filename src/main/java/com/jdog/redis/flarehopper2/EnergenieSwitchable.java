package com.jdog.redis.flarehopper2;

import com.jdog.redis.flarehopper2.dailytimer.Switchable;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.web.client.RestTemplate;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class EnergenieSwitchable implements Switchable {

    RestTemplate template;

    private static final String ON = "on";
    private static final String OFF = "off";

    private String current;

    private Disposable intervalDisposable;

    private Logger logger;

    public EnergenieSwitchable(RestTemplate template) {
        this.template = template;
        this.logger = LoggerFactory.getLogger(this.getClass());
        intervalDisposable = Disposables.single();
        current = "";
    }

    @Override
    public void on() {
        logger.info("command: on, current: %s", current);
        if( !ON.equals(current) ) {
            current = ON;
            if( !intervalDisposable.isDisposed()) intervalDisposable.dispose();
            intervalDisposable = Flux.interval(Duration.ZERO, Duration.ofSeconds(5))
                    .take(10)
                    .subscribe( n -> template.getForObject(ON, String.class));
        }
        System.out.println("energenie on");
    }

    @Override
    public void off() {
        logger.info("command: off, current: %s", current);

        if( !OFF.equals(current) ) {
        current = OFF;
        if( !intervalDisposable.isDisposed()) intervalDisposable.dispose();
        intervalDisposable = Flux.interval(Duration.ZERO, Duration.ofSeconds(5))
                .take(10)
                .subscribe( n -> template.getForObject(OFF, String.class));
    }
        System.out.println("energenie off");
    }
}
