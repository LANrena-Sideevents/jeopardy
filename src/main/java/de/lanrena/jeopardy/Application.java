package de.lanrena.jeopardy;

import de.lanrena.jeopardy.model.GlobalGameState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @SuppressWarnings("unused")
    public GlobalGameState getGlobalGameState() {
        return new GlobalGameState();
    }
}
