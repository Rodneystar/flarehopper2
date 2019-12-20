package com.jdog.redis.flarehopper2.dailytimer;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TimerEvent implements Serializable {

    private static final long serialVersionUID = 5109614238405878463L;

    private LocalTime startTime;
    private Duration duration;


    public TimerEvent(LocalTime start, Duration duration) {
        this.startTime = start;
        this.duration = duration;
    }
    public TimerEvent() {}

    public LocalTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public static Boolean isOverMidnight(TimerEvent event) {
        return event.getEndTime().isBefore(event.getStartTime());
    }

    public Boolean isOverMidnight() {
        return isOverMidnight(this);
    }

    public static Boolean areOverlapping(TimerEvent event1, TimerEvent event2){
        List<TimerEvent> list = Arrays.asList(event1,event2);
        list.sort(Comparator.comparing(timerEvent -> timerEvent.startTime));

        TimerEvent first = list.get(0); TimerEvent second = list.get(1);
        return (first.getEndTime().isAfter(second.getStartTime()) || first.isOverMidnight()) ||
                (second.getEndTime().isAfter(first.getStartTime()) && second.isOverMidnight());
    }

    public LocalTime getEndTime() {
        return startTime.plus(duration);
    }

    public Boolean isOverlapping(TimerEvent event) {
        return areOverlapping(this, event);
    }

    public long getMinsUntilStart() {
        long startTimeFromNow = LocalTime.now().until(this.startTime, ChronoUnit.MINUTES);
        return startTimeFromNow >= 0
                ? startTimeFromNow
                : startTimeFromNow + Duration.ofDays(1).toMinutes();

    }

    public Boolean isActiveAt(LocalTime atTime) {
        Boolean result;
        if(!this.isOverMidnight()) {
            result = this.getStartTime().isBefore(atTime) && this.getEndTime().isAfter(atTime);
        } else {
            result = atTime.isAfter(this.getStartTime()) || atTime.isBefore(this.getEndTime());
        }
        return result;
    }
}
