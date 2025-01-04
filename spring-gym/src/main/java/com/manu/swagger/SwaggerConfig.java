package com.manu.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info =
        @Info(
            contact =
                @Contact(
                    name = "Manuel Lucena",
                    email = "mlucenasalas@gmail.com",
                    url = "https://github.com/manulucena12"),
            description = "This documentation provides information to the Gym API",
            title = "Spring Gym Backend - Manuel Lucena"),
    servers = {@Server(url = "http://localhost:3002", description = "Local environment")},
    security = {@SecurityRequirement(name = "bearerAuth")})
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT Auth Token",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {}
