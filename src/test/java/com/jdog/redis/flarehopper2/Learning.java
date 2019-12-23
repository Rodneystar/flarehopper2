package com.jdog.redis.flarehopper2;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import com.jdog.redis.flarehopper2.FlarehopperService.FlarehopperMode;

import com.jdog.redis.flarehopper2.dailytimer.TimerEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.scheduler.VirtualTimeScheduler;

// @Disabled
public class Learning {


    @Test
    public void httpClient() throws IOException, InterruptedException {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate template = builder.uriTemplateHandler(
                new DefaultUriBuilderFactory("http://192.168.0.28:5000/")).build();
//        template.getForObject("/1/on", String.class);

        System.out.println(template.getForObject("/1/off", String.class));
    }
    @Test
    public void objOutputs() throws ClassNotFoundException, IOException, URISyntaxException {
        File os = new File("teststate");

        FileOutputStream fos = new FileOutputStream(os);
        ObjectOutputStream outPutStream = new ObjectOutputStream(fos);

        AppState state = new AppState();
        state.currentMode  = FlarehopperMode.ON;
        state.eventList = Arrays.asList(new TimerEvent(LocalTime.of(1, 30), Duration.ofMinutes(60)));
        System.out.println(state.eventList.get(0).getDuration());
        outPutStream.writeObject(state);

    }

    @Test
    public void parse_times() {
       Duration parsed = Duration.parse("PT120M");
       LocalTime parsedTime = LocalTime.parse("19:30");
        System.out.println(parsedTime.getHour());
        System.out.println(parsed.toHours());
    }


    @Test
    public void math() {
        System.out.println( 120 *60);
    }


      @Test
    public void objOutpsuts() {
        System.out.println(

        FlarehopperMode.OFF.toString());
    }

        @Test
    public void objOutput() throws ClassNotFoundException, IOException {
        File os = new File("teststate");
        FileInputStream fos = new FileInputStream(os);

        ObjectInputStream inputStream = new ObjectInputStream(fos);
            AppState state = (AppState) inputStream.readObject();

        System.out.println(state.currentMode);
        System.out.println(state.eventList.get(0).getDuration());
        System.out.println(state.eventList.get(0).getStartTime());


    }
    @Test
    public void testDisposed() {
        VirtualTimeScheduler.getOrSet();
        VirtualTimeScheduler scheduler = VirtualTimeScheduler.get();

        Disposable disposable = Mono.delay(Duration.ofMillis(100)).subscribe(System.out::println);
        System.out.println(disposable.isDisposed());
        scheduler.advanceTimeBy( Duration.ofSeconds(1));
        System.out.println(disposable.isDisposed());
    }


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
