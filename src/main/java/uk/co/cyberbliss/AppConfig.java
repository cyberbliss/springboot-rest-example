package uk.co.cyberbliss;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public BookRepository booksRepo(){
        return new BookRepository();
    }
}
