package com.jdog.redis.flarehopper.dailytimer;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class TimerModule {

    private TimerEventList timerEvents; 
    private Switchable switchable;
    private Disposable timerDisposable;
    private Scheduler scheduler;

    public TimerModule(Switchable switchable) {
        this(switchable, Schedulers.boundedElastic(), new TimerEventList());

    }
    

	public TimerModule(Switchable switchable, Scheduler scheduler, TimerEventList eventList) {
        this.switchable = switchable;
        this.scheduler = scheduler;
        this.timerEvents = eventList;
        this.timerDisposable = Disposables.single();
	}


	public void removeTimer(int index) {
        timerEvents.removeEvent(index);
    }

    public void setSwitchable( Switchable s) {
        this.switchable = s;
    }

    public void addTimer(TimerEvent event) {
        timerEvents.addEvent(event);
        refreshTimers();
    }

    private void refreshTimers() {
        timerDisposable.dispose();
        Flux<String> x = Flux.fromIterable(timerEvents.getAllEvents())
            .flatMap(e -> Flux.merge(
                    Flux.interval(Duration.ofMinutes(e.getMinsUntilStart()), Duration.ofDays(1), scheduler)
                        .map(l -> "on"),
                    Flux.interval(
                            Duration.ofMinutes(e.getMinsUntilStart() + e.getDuration().toMinutes()),
                            Duration.ofDays(1),
                            scheduler)
                        .map(l -> "off")));
        
        timerDisposable = x.subscribe( s -> {
            if(s.equals("on")) switchable.on();
            else switchable.off();
        });

        timerEvents.anyActiveAt(LocalTime.now())
            .subscribe( b -> {
                if( b ) switchable.on();
                else switchable.off();
            }, e -> e.printStackTrace());
    }

    public List<TimerEvent> getEvents() {
        return timerEvents.getAllEvents();
    }
}
