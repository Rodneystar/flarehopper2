package com.jdog.redis.flarehopper2;

import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;
import com.jdog.redis.flarehopper2.dailytimer.Switchable;
import com.jdog.redis.flarehopper2.dailytimer.TimerEvent;
import com.jdog.redis.flarehopper2.dailytimer.TimerEventList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


class FlarehopperServiceTest {


    FlarehopperService service;

    Switchable switchable;

    VirtualTimeScheduler testScheduler;

    TimerEventList list;

    AtomicInteger onCounter;

    AtomicInteger offCounter;


    @BeforeEach
    void setUp() {
        list =  new TimerEventList();
        list.addEvent( new TimerEvent( LocalTime.of(3, 30), Duration.ofMinutes(120)));
        testScheduler = VirtualTimeScheduler.getOrSet();
        switchable = Mockito.mock(Switchable.class);
        onCounter = new AtomicInteger(0);
        offCounter = new AtomicInteger(0);
        Mockito.doAnswer( invocation -> onCounter.getAndIncrement() ).when(switchable).on();
        Mockito.doAnswer( invocation -> offCounter.getAndIncrement() ).when(switchable).off();

        service = new FlarehopperService(new DailyTimerControl(switchable, testScheduler, list));
    }

    @Test
    public void service_initialized_goodDefaultSettings() {
        assertThat(service.getCurrentMode()).isEqualTo(FlarehopperService.FlarehopperMode.TIMED);
        assertThat(service.getRunBackUntil()).isBefore(LocalDateTime.now());
    }

    @Test
    public void service_runbackCalledWithCurrentModeRunback_durationIsAdded() {
        int initOffCounter = offCounter.get();

        service.modeRunback(Duration.ofMinutes(60));
        assertThat(onCounter.get()).isEqualTo(1);
        assertThat(offCounter.get()).isEqualTo( initOffCounter );

        testScheduler.advanceTimeBy(Duration.ofHours(2));

        assertThat(onCounter.get()).isEqualTo(1);
        assertThat(offCounter.get()).isEqualTo( initOffCounter + 1 );
    }
}