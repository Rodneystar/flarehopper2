package com.jdog.redis.flarehopper2;

import com.jdog.redis.flarehopper2.dailytimer.Switchable;
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

    public EnergenieSwitchable(RestTemplate template) {
        this.template = template;
        current = OFF;
        intervalDisposable = Disposables.single();
    }

    @Override
    public void on() {
        if( current != ON ) {
            if( !intervalDisposable.isDisposed()) intervalDisposable.dispose();
            intervalDisposable = Flux.interval(Duration.ofSeconds(20))
                    .take(5)
                    .subscribe( n -> template.getForObject(ON, String.class));
        }
        System.out.println("energenie on");
    }

    @Override
    public void off() {
        if( current != OFF ) {
        if( !intervalDisposable.isDisposed()) intervalDisposable.dispose();
        intervalDisposable = Flux.interval(Duration.ofSeconds(20))
                .take(5)
                .subscribe( n -> template.getForObject(ON, String.class));
    }
        System.out.println("energenie off");
    }
}
