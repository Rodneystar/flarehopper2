package com.jdog.redis.flarehopper2;

import com.jdog.redis.flarehopper2.dailytimer.DailyTimerControl;
import com.jdog.redis.flarehopper2.dailytimer.TimerEventList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
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
	public EnergenieSwitchable getSwitchable(
		@Value("${flarehopper.powerport}") int port,
		@Value("${flarehopper.powerhost}") String hostname,
		@Value("${flarehopper.energenieSocketNumber}") int energenieSocketNumber,
		RestTemplateBuilder templateBuilder
			){
		RestTemplate template = templateBuilder
				.uriTemplateHandler(
					new DefaultUriBuilderFactory("http://" + hostname + ":" + port + "/"+ energenieSocketNumber + "/"))
				.build();
		return new EnergenieSwitchable( template );
	}
	@Bean
	public DailyTimerControl dailyTimerControl( EnergenieSwitchable switchable) {
 		return new DailyTimerControl(
 				switchable,
				Schedulers.parallel(),
				new TimerEventList());
	}
}
