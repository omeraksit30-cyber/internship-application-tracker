package com.omeraksit.internshiptracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI internshipApplicationTrackerOpenApi() {
		Contact contact = new Contact()
				.name("Ömer Akşit")
				.email("omeraksit30@gmail.com");

		License license = new License()
				.name("MIT License");

		Info info = new Info()
				.title("Internship Application Tracker API")
				.version("v1")
				.description(
						"REST API for creating, tracking, filtering and managing internship applications."
				)
				.contact(contact)
				.license(license);

		return new OpenAPI().info(info);
	}
}
