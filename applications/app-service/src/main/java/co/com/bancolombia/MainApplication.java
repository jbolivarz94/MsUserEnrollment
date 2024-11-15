package co.com.bancolombia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.reactivecommons.async.impl.config.annotations.EnableMessageListeners;

@SpringBootApplication
@EnableMessageListeners
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
