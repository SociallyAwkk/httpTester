package com.goldpiecessoftware.httptester.http.model;

import java.net.http.HttpResponse;
import java.time.Duration;

public record RequestResponseInfo(HttpResponse<String> response, Duration responseTime){
}
