package com.app.catapi.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "CatAPI",
                version = "1.0.0",
                description = "REST API that consumes TheCatAPI to retrieve cat breeds and images. Requires JWT authentication for most endpoints.",
                contact = @Contact(
                        name = "Jean Carlo Montoya Castro",
                        email = "jcarlomontoya04@gmail.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development server"),
                @Server(url = "http://localhost:8000", description = "Docker server")
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Enter your JWT token",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}
