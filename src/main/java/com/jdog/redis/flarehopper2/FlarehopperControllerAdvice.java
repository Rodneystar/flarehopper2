package com.jdog.redis.flarehopper2;

import com.jdog.redis.flarehopper2.dailytimer.TimerOverLappingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FlarehopperControllerAdvice {

    @ExceptionHandler
    public void modeToRunback(IllegalStateException bre, HttpServletResponse res) {
        res.setStatus(400);
    }

    @ExceptionHandler
    public void timerOverlapping(TimerOverLappingException tol, HttpServletResponse res)  {
        res.setStatus(400);
    }

    @ExceptionHandler
    public void removeIndexOOB(IndexOutOfBoundsException oob, HttpServletResponse res)  {
        res.setStatus(200);
    }
}
