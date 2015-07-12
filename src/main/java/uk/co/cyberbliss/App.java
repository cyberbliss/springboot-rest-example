package uk.co.cyberbliss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ComponentScan
@EnableAutoConfiguration
public class App
{
    @Autowired
    private BookRepository booksRepo;


    public static void main( String[] args ){
        SpringApplication.run(App.class, args);

    }

    @PostConstruct
    public void initApplication() throws IOException {
        booksRepo.addBook(new Book("111-1","Java 8 Lamdas","Richard Warburton"));
        booksRepo.addBook(new Book("111-2","An Introduction to Programming in Go","Caleb Doxsey"));
    }
}
