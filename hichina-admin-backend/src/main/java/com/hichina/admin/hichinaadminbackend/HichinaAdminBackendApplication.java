package com.hichina.admin.hichinaadminbackend;

import com.hichina.admin.hichinaadminbackend.service.InitDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories
@EnableScheduling
public class HichinaAdminBackendApplication {

	@Autowired
	private InitDataService initDataService;

	public static void main(String[] args) {
		SpringApplication.run(HichinaAdminBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner CommandLineRunnerBean() {
		return (args) -> {
			initDataService.initAdminUser();
			initDataService.initProperty();
			initDataService.initProductType();
			initDataService.initProductTypeBinding();
		};
	}

}
