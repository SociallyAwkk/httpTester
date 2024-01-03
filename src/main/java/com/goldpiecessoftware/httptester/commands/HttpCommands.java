package com.goldpiecessoftware.httptester.commands;

import com.goldpiecessoftware.httptester.display.ProgressBar;
import com.goldpiecessoftware.httptester.http.HttpHandler;
import com.goldpiecessoftware.httptester.http.model.RequestResponseInfo;
import com.goldpiecessoftware.httptester.http.model.ResponseParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.standard.ShellComponent;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@ShellComponent
@Command(group = "http", description = "Commands for making HTTP requests")
public class HttpCommands {

    @Command(command = "get", description = "Makes GET requests to the specified URL with the specified number of requests and concurrent threads")
    public String get(@Option(longNames = {"--url"}, shortNames = {'u'}, description = "The endpoint to be requested", defaultValue = "http://192.168.0.29:5050/health") String url,
                      @Option(longNames = {"--requests"}, shortNames = {'r'}, description = "The amount of requests to action", defaultValue = "1") int requests,
                      @Option(longNames = {"--threads"}, shortNames = {'t'}, description = "The amount of concurrent threads to use", defaultValue = "10") int threads) {
        return requests(url, requests, threads, HttpMethod.GET.name());
    }

    @Command(command = "post", description = "Makes POST requests to the specified URL with the specified number of requests and concurrent threads")
    public String post(@Option(longNames = {"--url"}, shortNames = {'u'}, description = "The endpoint to be requested", defaultValue = "http://192.168.0.29:5050/health") String url,
                      @Option(longNames = {"--requests"}, shortNames = {'r'}, description = "The amount of requests to action", defaultValue = "1") int requests,
                      @Option(longNames = {"--threads"}, shortNames = {'t'}, description = "The amount of concurrent threads to use", defaultValue = "10") int threads) {
        return requests(url, requests, threads, HttpMethod.POST.name());
    }

    private String requests(String url, int requests, int threads, String httpMethod) {
        Instant start = Instant.now();
        log.debug("Making {} {} request{} to {} with {} concurrent thread{}",
                requests, httpMethod, (requests > 1 ? "s" : ""), url, threads, (threads > 1 ? "s" : ""));

        System.out.println("Making " + requests + " " + httpMethod + " request" + (requests > 1 ? "s" : "") +
                " to " + url + " with " + threads + " concurrent thread" + (threads > 1 ? "s" : ""));

        ProgressBar progressBar = new ProgressBar(requests);

        List<RequestResponseInfo> responses = HttpHandler.sendRequests(url, requests, httpMethod, threads, progressBar::update);

        progressBar.complete();

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        return ResponseParser.parseResponses(responses, duration.toMillis());
    }

}
