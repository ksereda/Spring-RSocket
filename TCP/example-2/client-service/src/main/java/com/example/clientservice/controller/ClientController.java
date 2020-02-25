package com.example.clientservice.controller;

import com.example.clientservice.model.Movie;
import com.example.clientservice.model.RequestMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClientController {

    private final RSocketRequester requester;

    @GetMapping("/movie/{id}")
    Mono<Movie> findMovieById(@PathVariable String id) {
        return this.requester
                .route("request-response")
//                .data(DefaultPayload.create(""))
                .data(new RequestMovie(id))
                .retrieveMono(Movie.class);
    }

    @GetMapping("/showAllMovies")
    Flux<Movie> findAllMovies() {
        return this.requester
                .route("request-stream")
                .retrieveFlux(Movie.class);
    }

    @PostMapping("/addMovie/{id}/{name}/{price}")
    Mono<Void> addMovie(@PathVariable String id,
                        @PathVariable String name,
                        @PathVariable String price) {
        return this.requester
                .route("fire-forget")
                .data(new Movie(id, name, price))
                .send();
    }
}