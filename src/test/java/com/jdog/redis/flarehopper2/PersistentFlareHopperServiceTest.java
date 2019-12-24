package com.jdog.redis.flarehopper2;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

import com.jdog.redis.flarehopper2.FlarehopperService.FlarehopperMode;
import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;
import com.jdog.redis.flarehopper2.dailytimer.Switchable;
import com.jdog.redis.flarehopper2.dailytimer.TimerEvent;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PersistentFlareHopperServiceTest {

    class TestSwitchable implements Switchable {
        public void on() {
            System.out.println("switchableOn");
        }

        public void off() {
            System.out.println("switchableOff");
        }
    }

    @Test
    public void testPersistence() {
        File os = new File("testservice");
        os.delete();
        TestSwitchable switchable = new TestSwitchable();
        PersistentFlareHopperService instance = new PersistentFlareHopperService(new DailyTimerControl(switchable),
                "testservice");
        instance.modeTimed();
        instance.addTimer(new TimerEvent(LocalTime.of(10, 45), Duration.ofMinutes(20)));

        try (FileInputStream fos = new FileInputStream(os);
                ObjectInputStream inputStream = new ObjectInputStream(fos);) {
            AppState state = (AppState) inputStream.readObject();
            Assertions.assertThat(state.eventList.size()).isEqualTo(1);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    public void testPersistencebackwads() {

        File os = new File("testservice");

        try {
            FileOutputStream fos = new FileOutputStream(os);
            ObjectOutputStream outPutStream = new ObjectOutputStream(fos);

            AppState state = new AppState();
            state.currentMode  = FlarehopperMode.ON;
            state.eventList = Arrays.asList(new TimerEvent(LocalTime.of(1, 30), Duration.ofMinutes(60)));
            System.out.println(state.eventList.get(0).getDuration());
            outPutStream.writeObject(state);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PersistentFlareHopperService service =
            new PersistentFlareHopperService(new DailyTimerControl(new TestSwitchable()), "testservice");

        Assertions.assertThat(service.getTimer().getEvents().size()).isEqualTo(1);
    }
}
