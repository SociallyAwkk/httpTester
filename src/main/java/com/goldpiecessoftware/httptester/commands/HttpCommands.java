package com.goldpiecessoftware.httptester.commands;

import com.goldpiecessoftware.httptester.display.ProgressBar;
import com.goldpiecessoftware.httptester.http.HttpHandler;
import com.goldpiecessoftware.httptester.http.model.RequestResponseInfo;
import com.goldpiecessoftware.httptester.http.model.ResponseParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@ShellComponent
public class HttpCommands {

    @ShellMethod(key = "get", value = "Makes GET requests to the specified URL with the specified number of requests and concurrent threads", group = "HTTP Commands")
    public String get(@ShellOption(value = {"--url", "-u"}, help = "The endpoint to be requested", defaultValue = "http://192.168.0.29:5050/health") String url,
                      @ShellOption(value = {"--requests", "-r"},help = "The amount of requests to action",defaultValue = "1") int requests,
                      @ShellOption(value = {"--threads", "-t"},help = "The amount of concurrent threads to use",defaultValue = "10") int threads) {
        return requests(url, requests, threads, HttpMethod.GET.name());
    }
    @ShellMethod(key = "post", value = "Makes POST requests to the specified URL with the specified number of requests and concurrent threads", group = "HTTP Commands")
    public String post(@ShellOption(value = {"--url", "-u"}, help = "The endpoint to be requested", defaultValue = "http://192.168.0.29:5050/health") String url,
                      @ShellOption(value = {"--requests", "-r"},help = "The amount of requests to action",defaultValue = "1") int requests,
                      @ShellOption(value = {"--threads", "-t"},help = "The amount of concurrent threads to use",defaultValue = "10") int threads) {
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
