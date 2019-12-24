package com.jdog.redis.flarehopper2.dailytimer;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TimerEventList {

    private List<TimerEvent> eventList;

    public TimerEventList() {
        eventList = new ArrayList<>();
    }

    public void addEvent(TimerEvent timerEvent) {
        if (!isOverlappingAny(timerEvent)) {
            eventList.add(timerEvent);
        } else {
            throw new TimerOverLappingException("timer was overlapping");
        }
    }

    public void removeEvent(int index) {
        eventList.remove(index);
    }

    private boolean isOverlappingAny(TimerEvent timerEvent) {
        Boolean qm = false;
        for (TimerEvent e : eventList) {
            qm = e.isOverlapping(timerEvent);
            if (qm)
                break;
        }
        return qm;
    }

    public List<TimerEvent> getAllEvents() {
        return eventList;
    }

    public Mono<Boolean> anyActiveAt(LocalTime atTime) {
        return Flux.fromIterable(eventList).any( e -> e.isActiveAt(atTime));
    }

	public void setEventList(List<TimerEvent> eventList) {
        this.eventList = eventList;
	}
}
