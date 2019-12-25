package com.jdog.redis.flarehopper2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jdog.redis.flarehopper2.dailytimer.TimerEvent;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlareController {


    FlarehopperService service;

    public FlareController( FlarehopperService service) {
        this.service = service;
    }

    @GetMapping("/mode")
    public Map<String, String> getMode() {
        HashMap<String, String> resBody = new HashMap<>();
        resBody.put("mode", service.getCurrentMode().toString() );
        return resBody;
    }

    @GetMapping("/timers")
    public List<TimerEvent> getTimers() {
        return service.getTimer().getEvents();
    }

    @PutMapping("/mode/{newMode}")
    public Map<String, String> putMode(@PathVariable("newMode") FlarehopperService.FlarehopperMode mode) {
        HashMap<String, String> resBody = new HashMap<>();
        service.modeSet(mode);
        resBody.put("mode", mode.toString() );
        return resBody;
    }

    @DeleteMapping("/timers/{index}")
    public void deleteTimer(@PathVariable Integer index) {
        service.removeTimer(index);
    }

    @PostMapping("/timers/add")
    @ResponseStatus(HttpStatus.CREATED)
    public TimerEvent postTimer(@RequestBody TimerEvent event) {
        service.addTimer(event);
        return event;
    }




}
