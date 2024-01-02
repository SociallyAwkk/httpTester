package com.goldpiecessoftware.httptester.display;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProgressBar {
    private final int totalRequests;
    private int currentRequest;

    public ProgressBar(int totalRequests) {
        this.totalRequests = totalRequests;
        this.currentRequest = 0;
    }

    public synchronized void update(int completedRequests) {
        currentRequest += completedRequests;
        if(currentRequest > totalRequests){
            log.debug("Current request count is greater than total request count");
            return;
        }
        double progress = (double) currentRequest / totalRequests;
        int barWidth = 50;
        int progressBar = (int) (progress * barWidth);

        System.out.print("\r[");
        for (int i = 0; i < progressBar; i++) {
            System.out.print("=");
        }
        for (int i = progressBar; i < barWidth; i++) {
            System.out.print(" ");
        }
        System.out.print("] " + currentRequest + "/" + totalRequests);
        System.out.flush();

        if (currentRequest == totalRequests) {
            System.out.println(); // Move to the next line after completion
        }
    }

    public synchronized void complete() {
        if (currentRequest < totalRequests) {
            // Complete the progress bar if not already completed
            update(totalRequests - currentRequest);
        }
    }
}
