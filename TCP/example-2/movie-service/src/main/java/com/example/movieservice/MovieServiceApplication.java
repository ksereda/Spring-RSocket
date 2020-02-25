package com.example.movieservice;

import com.example.movieservice.model.Movie;
import com.example.movieservice.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class MovieServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieServiceApplication.class, args);
	}

	// This code creates a Flux of four sample Persons objects, saves them to the DB. Then, queries all the Persons from the DB and print them to the console.
	@Bean
	CommandLineRunner run(MovieRepository movieRepository) {
		return args -> {
			movieRepository.deleteAll()
					.thenMany(Flux.just(
							new Movie("1", "Lion King", "130"),
							new Movie("2", "Saw", "200"),
							new Movie("3", "Home Alone", "150"),
							new Movie("4", "Home Alone 2", "180"),
							new Movie("5", "Interstellar", "300"),
							new Movie("6", "Prometheus", "80")
					)
							.flatMap(movieRepository::save))
					.thenMany(movieRepository.findAll())
					.subscribe(System.out::println);

		};
	}

}
