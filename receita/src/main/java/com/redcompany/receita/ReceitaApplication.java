package com.redcompany.receita;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.redcompany.receita.infra.property.WM3ApiProperty;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
@EnableConfigurationProperties(WM3ApiProperty.class)
@EnableScheduling
public class ReceitaApplication {

	public static void main(String[] args) {        
		SpringApplication.run(ReceitaApplication.class, args);
	}


}
