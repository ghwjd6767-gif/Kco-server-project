package com.sparta.kcoserverproject;

import org.springframework.boot.SpringApplication;

public class TestKcoServerProjectApplication {

	public static void main(String[] args) {
		SpringApplication.from(KcoServerProjectApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
