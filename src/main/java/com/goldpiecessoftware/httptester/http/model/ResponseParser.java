package com.goldpiecessoftware.httptester.http.model;

import com.goldpiecessoftware.httptester.display.ResponsePrinter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResponseParser {
    public static String parseResponses(List<RequestResponseInfo> responses, long totalTimeMillis){
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

        return ResponsePrinter.buildPrint(totalTimeMillis, totalRequests, shortestResponseTime, longestResponseTime, averageResponseTime, codeCountList);
    }
}
