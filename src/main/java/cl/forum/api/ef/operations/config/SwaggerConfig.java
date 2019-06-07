package cl.forum.api.ef.operations.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("cl.forum"))
				.paths(regex("/api.*"))
				.build()
				.apiInfo(metaInfo());
	}

	private ApiInfo metaInfo() {
		return new ApiInfo(
			"Documentacion Forum Rest Api", 
			"Documentation API Operations GFE",
			"1.0", 
			"http://termsofserviceurl.com", 
			new Contact(
				"ApiMantainer Group",
				"http://www.forum.api.cl", 
				"forum.api.mantainer@forum.cl"
			), 
			"Apache License", 
			"https://www.apache.org/licenses/LICENSE-2.0"
			);
		
	}
}