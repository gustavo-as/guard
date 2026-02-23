package com.operis.guard;

import org.springframework.boot.SpringApplication;
import com.operis.guard.config.RsaKeyConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyConfig.class)
public class GuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuardApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
