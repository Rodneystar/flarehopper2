package com.jdog.redis.flarehopper2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;

import com.jdog.redis.flarehopper2.FlarehopperService.FlarehopperMode;
import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;

public class PersistentFlareHopperService extends FlarehopperService {

    AppState state;

    ObjectOutputStream outputStream;

    public PersistentFlareHopperService(DailyTimerControl timerControl, String filename) {
        super(timerControl);

        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(this.getClass().getResourceAsStream(filename));
            state = (AppState) inputStream.readObject();
            load();
        } catch (IOException | ClassNotFoundException ce ) {
            state = new AppState();
            ce.printStackTrace();
        }

        // TODO Auto-generated constructor stub
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
        try {
            outputStream.writeObject(state);
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
