package com.goldpiecessoftware.httptester.http;

import com.goldpiecessoftware.httptester.http.model.RequestResponseInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class HttpHandler {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .build();

    public static List<RequestResponseInfo> sendRequests(String endpoint, int numRequests, String httpMethod, int concurrentThreads, Consumer<Integer> progressConsumer) {
        Semaphore semaphore = new Semaphore(concurrentThreads, true);
        List<CompletableFuture<RequestResponseInfo>> responseFutures = IntStream.range(0, numRequests)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> sendHttpRequestWithSemaphore(endpoint, httpMethod, semaphore)))
                .peek(future -> future.thenAccept(response -> progressConsumer.accept(1)))
                .toList();

        CompletableFuture<Void> combinedFutures = CompletableFuture.allOf(responseFutures.toArray(new CompletableFuture[0]));

        CompletableFuture<List<RequestResponseInfo>> allResponses = combinedFutures.thenApply(v ->
                responseFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );

        return allResponses.join();
    }

    private static RequestResponseInfo sendHttpRequestWithSemaphore(String endpoint, String httpMethod, Semaphore semaphore) {
        try {
            semaphore.acquire();
            return sendHttpRequest(endpoint, httpMethod);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error acquiring semaphore permit", e);
        } finally {
            semaphore.release();
        }
    }

    public static RequestResponseInfo sendHttpRequest(String endpoint, String httpMethod) {
        Instant requestStart = Instant.now();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .method(httpMethod, HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Instant requestEnd = Instant.now();
            Duration requestTime = Duration.between(requestStart, requestEnd);
            return new RequestResponseInfo(response, requestTime);
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error sending HTTP request", e);
        }
    }
}
