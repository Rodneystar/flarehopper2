package com.jdog.redis.flarehopper2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.util.ArrayList;

import com.jdog.redis.flarehopper2.FlarehopperService.FlarehopperMode;
import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;
import com.jdog.redis.flarehopper2.dailytimer.TimerEvent;
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
        this.state = new AppState();

        File file = new File(filename);

        try ( 
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream inputStream = new ObjectInputStream(fis)
            ) {
            this.state = (AppState) inputStream.readObject();
            load();
        } catch (IOException | ClassNotFoundException ce ) {
            System.out.println("FILED DOESNT EXIST YET--------------------");
        }
    }

    @Override
    public void addTimer(TimerEvent event) {
        super.addTimer(event);
        save();
    }

    @Override
    public void removeTimer(int index) {
        super.removeTimer(index);
        save();
    }

    private void load() {
        ArrayList<TimerEvent> eList = new ArrayList(state.eventList);
        timerControl.setEventList(eList);
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
