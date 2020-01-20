package com.jdog.redis.flarehopper2.dailytimer;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import reactor.test.scheduler.VirtualTimeScheduler;

public class DailyTimerControlTests {

    public DailyTimerControl out;

    public void setup() {
    }

    @Test
    public void timerModule_timersRefreshedWitchSingleEvent_switchableIsRun() throws InterruptedException {
        int onLatchCount = 5; int offLatchCount = 7;
        CountDownLatch onLatch = new CountDownLatch(onLatchCount);
        CountDownLatch offLatch = new CountDownLatch(offLatchCount);
        TimerEventList timerEventList = new TimerEventList();
        VirtualTimeScheduler testScheduler = VirtualTimeScheduler.getOrSet();


    class TestSwitchable implements Switchable{
        public void on() {
            System.out.println("switchableOn");
            onLatch.countDown();}
        public void off() {
            System.out.println("switchableOff");
            offLatch.countDown();}
    }

        out = new DailyTimerControl( new TestSwitchable(), testScheduler, timerEventList);

        assertThat(offLatch.getCount()).isEqualTo(offLatchCount - 0);    
        assertThat(onLatch.getCount()).isEqualTo(onLatchCount- 0);    

        out.addTimer(new TimerEvent(LocalTime.now().plus(1, ChronoUnit.HOURS), Duration.ofHours(1)));
        out.refreshTimers();

        assertThat(offLatch.getCount()).isEqualTo(offLatchCount - 1);
        assertThat(onLatch.getCount()).isEqualTo(onLatchCount- 0);    

        testScheduler.advanceTimeBy(Duration.ofHours(24));
        assertThat(offLatch.getCount()).isEqualTo(offLatchCount - 2);    
        assertThat(onLatch.getCount()).isEqualTo(onLatchCount- 1);    


        out.addTimer(new TimerEvent(LocalTime.now().plus(5, ChronoUnit.HOURS), Duration.ofHours(1)));
        out.refreshTimers();
        assertThat(onLatch.getCount()).isEqualTo(onLatchCount- 1);    
        assertThat(offLatch.getCount()).isEqualTo(offLatchCount - 3);    
        testScheduler.advanceTimeBy(Duration.ofHours(24));
        assertThat(offLatch.getCount()).isEqualTo(offLatchCount - 5);    
        assertThat(onLatch.getCount()).isEqualTo(onLatchCount- 3);  
        
        testScheduler.advanceTimeBy(Duration.ofHours(24));
        assertThat(offLatch.getCount()).isEqualTo(offLatchCount - 7);    
        assertThat(onLatch.getCount()).isEqualTo(onLatchCount- 5);  


        System.out.println("scheduled tasks " + testScheduler.getScheduledTaskCount());

        onLatch.await(5, TimeUnit.SECONDS);
        offLatch.await(5, TimeUnit.SECONDS);

    }

}