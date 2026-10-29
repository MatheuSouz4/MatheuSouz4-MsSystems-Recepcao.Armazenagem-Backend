package br.com.mssystems;

import org.springframework.boot.SpringApplication;

public class TestMssystemsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(MssystemsBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
