package com.example.LeaveManagementSystem.persistence.configuration;

import org.springframework.context.annotation.Configuration;



import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;


    @Configuration
    @OpenAPIDefinition(
            info = @Info(
                    title = "Leave Management System API",
                    version = "1.0",
                    description = "API documentation for the Leave Management System",
                    contact = @Contact(
                            name = "Bhavani K",
                            email = "bhavani.k@aaludra.com",
                            url = "http://localhost:8080"
                    )
            ),
            servers = {
                    @Server(
                            description = "Local Server",
                            url = "http://localhost:8080"
                    )
            }

    )


public class SwaggerConfig {
}
