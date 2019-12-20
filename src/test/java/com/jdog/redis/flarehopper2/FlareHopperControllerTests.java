package com.jdog.redis.flarehopper2;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;
import com.jdog.redis.flarehopper2.dailytimer.TimerEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FlareHopperControllerTests {

    MockMvc mockMvc;

    FlarehopperService service;

    FlareController controller;

    ObjectMapper mapper;

    DailyTimerControl timer;

    @BeforeEach
    public void setupClass() {
        service = Mockito.mock(FlarehopperService.class);
        timer = Mockito.mock(DailyTimerControl.class);
        when(service.getTimer()).thenReturn(timer);

        controller = new FlareController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
        mapper = Jackson2ObjectMapperBuilder.json().build();
    }

    @Test
    public void checkMapper() {
        System.out.println(mapper);
    }
    @Test
    public void postTimer_callsAddTimerOnService() throws Exception {
        Map<String, String> addTimerReqBody = new HashMap();
        addTimerReqBody.put("startTime", "19:30");
        addTimerReqBody.put("duration", "PT120M");
        String reqBodyString = mapper.writeValueAsString(addTimerReqBody);


        mockMvc.perform( post("/timers/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBodyString))
                .andExpect( status().isCreated() );

        ArgumentCaptor<TimerEvent> eventArgumentCaptor = ArgumentCaptor.forClass(TimerEvent.class);
        verify(timer, times(1)).addTimer(eventArgumentCaptor.capture());
        TimerEvent passedEvent = eventArgumentCaptor.getValue();
        assertThat(passedEvent.getDuration()).isEqualTo(Duration.ofHours(2));
    }

    @Test
    public void deleteEvent_callsDeleteOnTimerList() throws Exception {
        mockMvc.perform( delete("/timers/delete/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect( status().isOk() );

        verify(timer, times(1)).removeTimer( eq(0));
    }
}
