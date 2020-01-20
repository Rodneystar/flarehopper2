package com.jdog.redis.flarehopper2.dailytimer;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class DailyTimerControl {

    private TimerEventList timerEvents; 
    private Switchable switchable;
    private Disposable timerDisposable;
    private Scheduler scheduler;

    public DailyTimerControl(Switchable switchable) {
        this(switchable, Schedulers.parallel(), new TimerEventList());

    }
    

	public DailyTimerControl(Switchable switchable, Scheduler scheduler, TimerEventList eventList) {
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
    }

    public void refreshTimers() {
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

    public void activate() {
        refreshTimers();
    }

    public void solidOn() {
        stopTimers();
        switchable.on();
    }

    public void solidOff() {
        stopTimers();
        switchable.off();
    }


    private void stopTimers() {
        timerDisposable.dispose();
    }


	public void setEventList(List<TimerEvent> eventList) {
        this.timerEvents.setEventList(eventList);
	}
}
