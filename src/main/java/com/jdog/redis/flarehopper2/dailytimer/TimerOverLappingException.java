package com.jdog.redis.flarehopper2.dailytimer;

public class TimerOverLappingException extends RuntimeException {

    
    /**
     *
     */
    private static final long serialVersionUID = 172834123471735182L;

    public TimerOverLappingException(String message) {
        super(message);
    }
}
