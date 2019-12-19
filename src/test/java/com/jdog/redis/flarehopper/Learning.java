package com.jdog.redis.flarehopper;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.scheduler.VirtualTimeScheduler;

public class Learning {

    @Test
    public void testBUffer() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[] { 1, 5, 2, 3, 4 });
        System.out.println(bb.remaining());

        System.out.println(bb.get());
        System.out.println(bb.remaining());

    }

    @Test
    public void testLocalTime() {
        LocalTime timeNow = LocalTime.now();
        System.out.println(timeNow);
        System.out.println(timeNow.plus(Duration.ofHours(2)));

    }

    @Test
    public void testFluxAny() {
        Mono<Boolean> result = Flux.just(false, true, false, false, false, false).any(n -> n);

        System.out.println("result: " + result.block());
    }

    @Test
    public void testVirtualTimeSched() throws InterruptedException {
        VirtualTimeScheduler.getOrSet();
        VirtualTimeScheduler scheduler = VirtualTimeScheduler.get();
        Scheduler anotherScheduler = Schedulers.boundedElastic();
        System.out.printf("%s, %n %s", scheduler.getClass(), anotherScheduler.getClass());

        CountDownLatch latch = new CountDownLatch(3);
        Flux.interval(Duration.ofMinutes(3)).subscribe(l -> latch.countDown());

        System.out.println(scheduler.getScheduledTaskCount());
        Thread.sleep(1000);
        scheduler.advanceTimeBy(Duration.ofMinutes(3));
        System.out.printf("latchCount: %s%n", latch.getCount());
        System.out.println(scheduler.getScheduledTaskCount());

        Thread.sleep(1000);
        scheduler.advanceTimeBy(Duration.ofMinutes(3));
        System.out.printf("latchCount: %s%n", latch.getCount());
        System.out.println(scheduler.getScheduledTaskCount());

        Thread.sleep(1000);
        scheduler.advanceTimeBy(Duration.ofMinutes(3));
        System.out.printf("latchCount: %s%n", latch.getCount());
        System.out.println(scheduler.getScheduledTaskCount());

        latch.await();

    }

    @Test
    public void testIntervalsDisposed() throws InterruptedException {
        Disposable disposable = Flux.merge(
            Flux.interval(Duration.ofMillis(200)).map(l -> "first"),
            Flux.interval(Duration.ofMillis(100), Duration.ofMillis(200)).map(l -> "second"))
                .subscribe(System.out::println);
            Thread.sleep(5000);
            System.out.println("after 5 seconds... ");

            disposable.dispose();
            disposable = Flux.merge(
                Flux.interval(Duration.ofMillis(200)).map(l -> "third"),
                Flux.interval(Duration.ofMillis(100), Duration.ofMillis(200)).map(l -> "fourth"))
                    .subscribe(System.out::println);
            Thread.sleep(5000);
    }
}
