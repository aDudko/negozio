package net.dudko.project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@OpenAPIDefinition(
        info = @Info(
                title = "Negozio",
                description = "Negozio REST API Documentation",
                version = "v1.0",
                contact = @Contact(
                        name = "Dudko Anatol",
                        email = "anatoly_dudko@icloud.com"
                ),
                license = @License(
                        name = "GPL-3.0 license",
                        url = "https://github.com/aDudko/negozio?tab=GPL-3.0-1-ov-file"
                )
        )
)
@SpringBootApplication
public class NegozioApplication {

    public static void main(String[] args) {
        SpringApplication.run(NegozioApplication.class, args);
    }

}
