package com.goldpiecessoftware.httptester.commands;

import com.goldpiecessoftware.httptester.display.ProgressBar;
import com.goldpiecessoftware.httptester.display.Table;
import com.goldpiecessoftware.httptester.http.HttpHandler;
import com.goldpiecessoftware.httptester.http.model.RequestResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        log.info("Making {} {} requests to {} with {} concurrent threads", requests,httpMethod, url, threads);

        ProgressBar progressBar = new ProgressBar(requests);

        List<RequestResponseInfo> responses = HttpHandler.sendRequests(url, requests, httpMethod, threads, progressBar::update);

        progressBar.complete();

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        return parseResponses(responses, duration.toMillis());
    }


    private String parseResponses(List<RequestResponseInfo> responses, long totalTimeMillis){
        int totalRequests = responses.size();
        Map<Integer, Integer> responseCodeCounts = new HashMap<>();
        Duration shortestResponseTime = Duration.ofDays(1);
        Duration longestResponseTime = Duration.ZERO;
        Duration totalResponseTime = Duration.ZERO;

        for (RequestResponseInfo responseInfo : responses) {
            int statusCode = responseInfo.response().statusCode();
            Duration responseTime = responseInfo.responseTime();

            responseCodeCounts.put(statusCode, responseCodeCounts.getOrDefault(statusCode, 0) + 1);

            shortestResponseTime = responseTime.compareTo(shortestResponseTime) < 0 ? responseTime : shortestResponseTime;
            longestResponseTime = responseTime.compareTo(longestResponseTime) > 0 ? responseTime : longestResponseTime;

            totalResponseTime = totalResponseTime.plus(responseTime);
        }

        List<Map.Entry<Integer, Integer>> codeCountList = responseCodeCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .toList();
        Duration averageResponseTime = totalResponseTime.dividedBy(totalRequests);

        return buildPrint(totalTimeMillis, totalRequests, shortestResponseTime, longestResponseTime, averageResponseTime, codeCountList);
    }

    private static String buildPrint(long totalTimeMillis, int totalRequests, Duration shortestResponseTime, Duration longestResponseTime, Duration averageResponseTime, List<Map.Entry<Integer, Integer>> codeCountList) {
        Map<String, String> metrics = new HashMap<>();
        metrics.put("Total Requests", String.valueOf(totalRequests));
        metrics.put("Shortest Response Time", shortestResponseTime.toMillis() + " ms");
        metrics.put("Longest Response Time", longestResponseTime.toMillis() + " ms");
        metrics.put("Average Response Time", averageResponseTime.toMillis() + " ms");
        Map<String, String> responseCodeMetrics = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : codeCountList) {
            responseCodeMetrics.put("Response Code " + entry.getKey(), String.valueOf(entry.getValue()));
        }
        Table metricTable = new Table("Metric", "Value", metrics);
        Table responseCodeTable = new Table("Response Code", "Count", responseCodeMetrics);
        return metricTable.toTable() + "\n" +
                responseCodeTable.toTable() +
                "\n" +
                "Total Time: " + totalTimeMillis + " milliseconds";
    }


}
