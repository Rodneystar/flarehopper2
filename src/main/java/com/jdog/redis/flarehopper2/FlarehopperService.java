package com.jdog.redis.flarehopper2;

import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

        timerControl.activate();
        currentMode = FlarehopperMode.TIMED;
    }

    private void cancelRunBack() {
        if(!runbackDisposable.isDisposed())
            runbackDisposable.dispose();
        runbackUntil = LocalDateTime.of(1999,12,30,23,59,59);

    }


    public void modeRunback(Duration d) {
        if(currentMode == FlarehopperMode.RUNBACK) {
            runbackUntil = runbackUntil.plus(d);
            runbackDisposable.dispose();
            timerControl.solidOn();
            runbackDisposable = Mono.delay(Duration.ofMinutes(
                    LocalDateTime.now().until(runbackUntil, ChronoUnit.MINUTES)))
                    .subscribe( l -> {
                        modeSet(prevMode);
                    });
        } else {
            prevMode = currentMode;
            runbackUntil = LocalDateTime.now().plus(d);
            timerControl.solidOn();
            runbackDisposable = Mono.delay(Duration.ofMinutes(
                    LocalDateTime.now().until(runbackUntil, ChronoUnit.MINUTES)))
                    .subscribe( l -> {
                        modeSet(currentMode);
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
