package com.jdog.redis.flarehopper2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class FlarehopperApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	FlarehopperService service;


	@BeforeEach
	public void setup() {
		service.factoryReset();
	}

	@Test
	public void deleteEvent_callsDeleteOnTimerList_returnsOkIfOutOfBounds() throws Exception {
		mockMvc.perform( delete("/timers/0")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect( status().isOk() );

	}



	@Test
	void getTimers_returnsJsonList() throws Exception {

		Map<String, String> addTimerReqBody = new HashMap<String, String>();
		addTimerReqBody.put("startTime", "19:30");
		addTimerReqBody.put("duration", "PT120M");
		String reqBodyString = mapper.writeValueAsString(addTimerReqBody);

		mockMvc.perform( post("/timers/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(reqBodyString))
				.andExpect( status().isCreated() );

		mockMvc.perform(get("/timers"))
				.andExpect(jsonPath("$[0].startTime")
					.value("19:30:00"))
				.andExpect(jsonPath("$[0].duration")
					.value("7200.0"));
	}



	@Test
	void getMode_returnsdefaultTimed() throws Exception {
		mockMvc.perform(get("/mode"))
				.andExpect(jsonPath("$.mode")
						.value("OFF"));
	}

	@Test
	void putModeRunback_badRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.put("/mode/RUNBACK"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}


	@Test
	void getMode_setModeTimed_returnsdefaultTimed() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.put("/mode/TIMED"))
				.andExpect(jsonPath("$.mode")
						.value("TIMED"));

		mockMvc.perform(get("/mode"))
				.andExpect(jsonPath("$.mode")
				.value("TIMED"));
	}

	@Test
	void postTImers_overlappingTimers_400() throws Exception {
		Map<String, String> addTimerReqBody = new HashMap<String, String>();
		addTimerReqBody.put("startTime", "19:30");
		addTimerReqBody.put("duration", "PT120M");
		String reqBodyString = mapper.writeValueAsString(addTimerReqBody);


		mockMvc.perform( post("/timers/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(reqBodyString))
				.andExpect( status().isCreated() );

		addTimerReqBody.put("startTime", "20:30");
		reqBodyString = mapper.writeValueAsString(addTimerReqBody);
		mockMvc.perform( post("/timers/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(reqBodyString))
				.andExpect( status().isBadRequest() );
	}


}
