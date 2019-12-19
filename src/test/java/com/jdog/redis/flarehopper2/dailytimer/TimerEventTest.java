package com.jdog.redis.flarehopper2.dailytimer;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class TimerEventTest {

    
    @Test
    public void givenEventSpanningCurrentTime_isActiveCalled_thenReturnsTrue() {
        class IsActiveTestCase{
            public IsActiveTestCase( TimerEvent event, LocalTime timeAt, Boolean expected){
                this.event = event; this.timeAt = timeAt; this.expected = expected;
            }
            public TimerEvent event; public LocalTime timeAt; public Boolean expected;
        }
        
        IsActiveTestCase[] testCases = new IsActiveTestCase[]{
            new IsActiveTestCase( new TimerEvent(LocalTime.of(01, 30), Duration.ofHours(1)), LocalTime.of(2, 0), true),
            new IsActiveTestCase( new TimerEvent(LocalTime.of(16, 20), Duration.ofMinutes(20)), LocalTime.of(16, 30), true),
            new IsActiveTestCase( new TimerEvent(LocalTime.of(16, 20), Duration.ofMinutes(20)), LocalTime.of(3, 30), false),
            new IsActiveTestCase( new TimerEvent(LocalTime.of(23, 20), Duration.ofHours(3)), LocalTime.of(3, 30), false),
            new IsActiveTestCase( new TimerEvent(LocalTime.of(23, 20), Duration.ofHours(3)), LocalTime.of(23, 10), false),            new IsActiveTestCase( new TimerEvent(LocalTime.of(23, 20), Duration.ofHours(3)), LocalTime.of(3, 30), false),
            new IsActiveTestCase( new TimerEvent(LocalTime.of(23, 20), Duration.ofHours(3)), LocalTime.of(2, 00), true),
            new IsActiveTestCase( new TimerEvent(LocalTime.of(23, 20), Duration.ofHours(3)), LocalTime.of(23, 23), true),
        };
        
        for( IsActiveTestCase thisCase : testCases) {
                assertThat(thisCase.event.isActiveAt(thisCase.timeAt))
                    .describedAs("%nexpected %s to be active for start: %s, end-time: %s%n",
                        thisCase.timeAt, thisCase.event.getStartTime(), thisCase.event.getEndTime())
                    .isEqualTo(thisCase.expected);
        }
    }

    @Test
    public void givenEventOnSameDay_WhenGetMinsUntilStartIsCalled_thenReturnCorrectTime() {
        TimerEvent event1 =
                new TimerEvent(LocalTime.now().plus(156,
                        ChronoUnit.MINUTES), Duration.ofHours(1));
                        System.out.println("Test");
        assertThat(event1.getMinsUntilStart()).isCloseTo(156L, Offset.offset(1L));
    }

    @Test
    public void givenEventOnSameDayAgain_WhenGetMinsUntilStartIsCalled_thenReturnCorrectTime() {
        TimerEvent event1 =
                new TimerEvent(LocalTime.now().plus(623,
                        ChronoUnit.MINUTES), Duration.ofHours(1));
        assertThat(event1.getMinsUntilStart()).isCloseTo(623L, Offset.offset(1L));

    }
    @Test
    public void givenEventNextDay_WhenGetMinsUntilStartIsCalled_thenReturnCorrectTime() {
        TimerEvent event1 =
                new TimerEvent(LocalTime.now().minus(5, ChronoUnit.MINUTES),
                        Duration.ofMinutes(2));

        assertThat(event1.getMinsUntilStart()).isCloseTo(Duration.ofDays(1).toMinutes() - 5, Offset.offset(1L));

    }
    @Test
    public void given2OverlappingEvents_whenIsOverLappingCalled_ReturnsTrue() {
        TimerEvent event1 = new TimerEvent(LocalTime.now(), Duration.ofHours(1));
        TimerEvent event2 = new TimerEvent(
                LocalTime.now().plus(55, ChronoUnit.MINUTES),
                Duration.ofHours(1)
        );
        assertThat(event1.isOverlapping(event2)).isTrue();
    }

    @Test
    public void given2NonOverlappingEvents_whenIsOverLappingCalled_ReturnsFalse() {
        TimerEvent event1 = new TimerEvent(LocalTime.now(), Duration.ofHours(1));
        TimerEvent event2 = new TimerEvent(
                LocalTime.now().plus(2, ChronoUnit.HOURS),
                Duration.ofHours(1)
        );
        assertThat(event1.isOverlapping(event2)).isFalse();
    }

    @Test
    public void given2OverlappingEventsOverMidnight_whenIsOverLappingCalled_ReturnsTrue() {
        TimerEvent event1 = new TimerEvent(LocalTime.of(23,30), Duration.ofHours(1));
        TimerEvent event2 = new TimerEvent(
                LocalTime.of(0, 14),
                Duration.ofHours(1)
        );
        assertThat(event1.isOverlapping(event2)).isTrue();
    }

    @Test
    public void given1EventInsideAnotherOverMidnight_whenIsOverLappingCalled_ReturnsTrue() {
        TimerEvent event1 = new TimerEvent(LocalTime.of(23,30), Duration.ofHours(1));
        TimerEvent event2 = new TimerEvent(
                LocalTime.of(23, 14),
                Duration.ofHours(2)
        );
        assertThat(event1.isOverlapping(event2)).isTrue();
    }
}
