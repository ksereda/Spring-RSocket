package com.example.client.controller;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RSocketController {

    private final RSocket rSocket;

    @GetMapping("/fire-and-forget")
    public Mono<Void> fireAndForget() {
        rSocket.fireAndForget(DefaultPayload.create("fire-and-forget!"))
                .subscribe(System.out::println);

        return Mono.empty();
    }

    @GetMapping("/request-response")
    public Mono<Payload> getRequestResponse() {
        return rSocket.requestResponse(DefaultPayload.create("request-response!"))
                .doOnNext(System.out::println);
    }

    @GetMapping("/request-stream")
    public Disposable getRequestStream() {
//        return rSocket.requestStream(DefaultPayload.create("request-stream!"));
        return rSocket.requestStream(DefaultPayload.create("request-stream!"))
                .delayElements(Duration.ofMillis(1000))
                .subscribe(
                        payload -> System.out.println(payload.getDataUtf8()),
                        e -> System.out.println("Error: " + e.toString()),
                        () -> System.out.println("Completed")
                );

    }

    @GetMapping(value = "channel")
    public Flux<String> getChannel() {
        return rSocket.requestChannel(Flux.interval(Duration.ofSeconds(2)).map(l -> DefaultPayload.create("ping ")))
                .map(Payload::getDataUtf8)
                .doOnNext(string ->log.info("Received: " + string))
                .take(20);
    }

}