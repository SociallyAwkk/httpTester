package com.goldpiecessoftware.httptester.display;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResponsePrinter {
    public static String buildPrint(long totalTimeMillis, int totalRequests, Duration shortestResponseTime, Duration longestResponseTime, Duration averageResponseTime, List<Map.Entry<Integer, Integer>> codeCountList) {
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
