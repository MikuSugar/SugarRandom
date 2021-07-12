package me.mikusugar.sugar.random.cli;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.jline.utils.AttributedString;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
@ComponentScan({"me.mikusugar.random.core", "me.mikusugar.sugar.random.cli"})
@EnableJpaRepositories("me.mikusugar.random.core")
@EntityScan("me.mikusugar.random.core")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }

    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("🍭:>");
    }

}
