package com.tunanc.filetemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.tunanc"})
public class FileTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileTemplateApplication.class, args);
	}

}
