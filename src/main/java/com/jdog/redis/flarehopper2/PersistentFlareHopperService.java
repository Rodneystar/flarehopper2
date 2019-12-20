package com.jdog.redis.flarehopper2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;

import com.jdog.redis.flarehopper2.FlarehopperService.FlarehopperMode;
import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;
import reactor.core.Disposables;

public class PersistentFlareHopperService extends FlarehopperService {

    AppState state;

    String filename;

    public PersistentFlareHopperService(DailyTimerControl timerControl, String filename) {
        super();
        this.timerControl = timerControl;
        this.currentMode = FlarehopperMode.OFF;
        this.runbackDisposable = Disposables.single();
        this.filename = filename;

        try ( ObjectInputStream inputStream =  new ObjectInputStream(
                        new FileInputStream(new File(filename)))) {
            state = (AppState) inputStream.readObject();
            load();
        } catch (IOException | ClassNotFoundException ce ) {

            state = new AppState();
            System.out.println("FILED DOESNT EXIST YET--------------------");
        }
    }

    private void load() {
        timerControl.setEventList(state.eventList);
        if (state.currentMode.equals(FlarehopperMode.RUNBACK)) {
            modeTimed();
        } else {
            modeSet(state.currentMode);
        }
    }
    
    private void save() {
        state.currentMode = currentMode;
        state.eventList = timerControl.getEvents();
        try ( ObjectOutputStream oos =
                      new ObjectOutputStream(
                              new FileOutputStream(
                                      new File(filename)))) {
            oos.writeObject(state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modeOn() {
        super.modeOn();
        save();
    }

    @Override
    public void modeOff() {
        super.modeOff();
        save();
    }

    @Override
    public void modeTimed() {
        super.modeTimed();
        save();
    }

    @Override
    public void modeRunback(Duration d) {
        super.modeRunback(d);
        save();
    }

    

}
