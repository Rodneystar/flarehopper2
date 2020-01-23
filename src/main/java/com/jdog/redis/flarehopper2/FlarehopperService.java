package com.jdog.redis.flarehopper2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;
import com.jdog.redis.flarehopper2.dailytimer.TimerEvent;

import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Mono;

public class FlarehopperService {

    protected DailyTimerControl timerControl;
    protected FlarehopperMode currentMode;
    protected FlarehopperMode prevMode;
    protected Disposable runbackDisposable;

    private LocalDateTime runbackUntil;

    public FlarehopperService(DailyTimerControl timerControl) {
        this.timerControl = timerControl;
        runbackDisposable = Disposables.single();
        modeTimed();
        cancelRunBack();
    }

    public FlarehopperService() {
    }

    public FlarehopperMode getCurrentMode() {
        return currentMode;
    }

    public LocalDateTime getRunBackUntil() {
        return runbackUntil;
    }

    public void factoryReset() {
        this.timerControl.getEvents().clear();
        this.modeOff();
    }

    public void addTimer(TimerEvent event) {
        this.timerControl.addTimer(event);
        if( FlarehopperMode.TIMED == getCurrentMode())
            this.timerControl.refreshTimers();
    }

    public void removeTimer(int index) {
        this.timerControl.removeTimer(index);
        if( FlarehopperMode.TIMED == getCurrentMode())
            this.timerControl.refreshTimers();
    }


    public enum FlarehopperMode {
        ON,
        OFF,
        TIMED,
        RUNBACK
    }

    public DailyTimerControl getTimer() {
        return this.timerControl;
    }

    public void modeOn() {
        if(currentMode == FlarehopperMode.RUNBACK) cancelRunBack();
        timerControl.solidOn();
        currentMode = FlarehopperMode.ON;
    }

    public void modeOff() {
        if(currentMode == FlarehopperMode.RUNBACK)cancelRunBack();

        timerControl.solidOff();
        currentMode = FlarehopperMode.OFF;
    }

    public void modeTimed() {
        if(currentMode == FlarehopperMode.RUNBACK) cancelRunBack();
        if(currentMode == FlarehopperMode.TIMED) return;
        timerControl.activate();
        currentMode = FlarehopperMode.TIMED;
    }

    private void cancelRunBack() {
        if(!runbackDisposable.isDisposed())
            runbackDisposable.dispose();
        runbackUntil = LocalDateTime.of(1999,12,30,23,59,59);

    }


    public void modeRunback(Duration d) {
        runbackUntil = LocalDateTime.now().plus(d);
        timerControl.solidOn();

        if(currentMode == FlarehopperMode.RUNBACK) {
            runbackDisposable.dispose();
            runbackDisposable = Mono.delay(d, timerControl.getScheduler())
                    .subscribe( l -> {
                        modeSet(prevMode);
                    });
        } else {
            prevMode = currentMode;
            currentMode = FlarehopperMode.RUNBACK;
            runbackDisposable = Mono.delay(d, timerControl.getScheduler())
                    .subscribe( l -> {
                        modeSet(prevMode);
                    });
        }
    }

    protected void modeSet(FlarehopperMode prevMode) {
        switch (prevMode) {
            case RUNBACK: throw new IllegalStateException("cant set runback from modeset");
            case ON:  modeOn(); break;
            case OFF: modeOff(); break;
            case TIMED: modeTimed(); break;
        }
    }


}
