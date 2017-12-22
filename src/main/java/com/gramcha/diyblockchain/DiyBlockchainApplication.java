package com.gramcha.diyblockchain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.gramcha.*", "com.gramcha.diyblockchain"})
public class DiyBlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiyBlockchainApplication.class, args);
	}
}
