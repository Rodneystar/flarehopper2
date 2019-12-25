package com.jdog.redis.flarehopper2;

import java.io.Serializable;
import java.util.List;

import com.jdog.redis.flarehopper2.FlarehopperService.FlarehopperMode;
import com.jdog.redis.flarehopper2.dailytimer.TimerEvent;

public class AppState implements Serializable{

	
    /**
     *
     */
    private static final long serialVersionUID = -7036273529921108382L;

    FlarehopperMode currentMode;

    List<TimerEvent> eventList;

    public AppState() {


    }
    


}
