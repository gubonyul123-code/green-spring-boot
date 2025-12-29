package com.green.green;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync    // 비동기 처리 (외부 요청하는 건 비동기가 낫다)
public class GreenApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenApplication.class, args);
	}

}
