package cn.ipanel.authorization.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ConcurrentHashMap;

@EnableScheduling
@ServletComponentScan
@SpringBootApplication
public class   DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public ConcurrentHashMap<String, Long> pcManagerActiveTime() {
		return new ConcurrentHashMap<>(100);
	}

}
