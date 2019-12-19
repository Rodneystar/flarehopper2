package com.jdog.redis.flarehopper2.dailytimer;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimerEventListTest {

    TimerEventList list;

    @BeforeEach
    public void setUp() throws Exception {
        list = new TimerEventList();
    }

    @Test
    public void givenEmptyTimerList_whenEventsAreAdded_thenGetEventsReturnsObservable() {
        List<TimerEvent> underTest = list.getAllEvents();

        assertThat(underTest).isEmpty();

        list.addEvent(new TimerEvent(LocalTime.of(12,0), Duration.ofHours(2)));
        assertThat( underTest.size()).isEqualTo(1);
        assertThat(underTest.get(0).getDuration()).isEqualTo(Duration.ofHours(2));
    }

    @Test
    public void overlappingEvents_secondEvent_throwTimerOverlappingException() {
        Boolean exCaught = false;
        try{
            list.addEvent(new TimerEvent(LocalTime.of(12,0), Duration.ofHours(2)));
            list.addEvent(new TimerEvent(LocalTime.of(13,56), Duration.ofHours(2)));
        } catch( TimerOverLappingException e) {
            exCaught = true;
        }

        assertThat(exCaught).isTrue();

        assertThat(list.getAllEvents().size()).isEqualTo(1);
    }
}
