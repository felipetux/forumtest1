package cl.forum.api.ef.operations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import cl.forum.api.ef.operations.config.Properties;

@SpringBootApplication
@EnableConfigurationProperties(Properties.class)
public class OperationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationsApplication.class, args);
	}
	
}
