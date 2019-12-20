package com.jdog.redis.flarehopper2;

import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;
import com.jdog.redis.flarehopper2.dailytimer.TimerEventList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class FlarehopperApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlarehopperApplication.class, args);
	}

	@Bean
	public FlarehopperService flarehopperService(
			DailyTimerControl timerControl,
			@Value("${flarehopper.filename}") String filename) {
		return new PersistentFlareHopperService(timerControl, filename);
	}


	@Bean
	public DailyTimerControl dailyTimerControl() {
 		return new DailyTimerControl(
 				new EnergenieSwitchable(),
				Schedulers.parallel(),
				new TimerEventList());
	}
}
