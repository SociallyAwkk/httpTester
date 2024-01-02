package com.goldpiecessoftware.httptester.display;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Table {
    private String firstColumnName;
    private String secondColumnName;
    private int firstColumnWidth = 22;
    private int secondColumnWidth = 11;
    private Map<String, String> data;

    public Table(String metric, String value, Map<String, String> metrics) {
        this.firstColumnName = metric;
        this.secondColumnName = value;
        this.data = metrics;
    }

    public String toTable(){
        StringBuilder builder = new StringBuilder();

        String header = breakLine() +
                formatLine(this.firstColumnName, this.secondColumnName) +
                breakLine();

        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> entry : this.data.entrySet()) {
            data.append(formatLine(entry.getKey(), entry.getValue()));
        }

        builder.append(header).append(data).append(breakLine());
        return builder.toString();
    }
    private String formatLine(String metric, String value) {
        return String.format("| %-" + this.firstColumnWidth + "s | %-" + this.secondColumnWidth + "s |\n", metric, value);
    }
    private String breakLine(){
        return String.format("+%-"+ (this.firstColumnWidth + 2) +"s+%-"+ (this.secondColumnWidth + 2) +"s+\n", "-".repeat(this.firstColumnWidth + 2), "-".repeat(this.secondColumnWidth + 2));
    }

}

