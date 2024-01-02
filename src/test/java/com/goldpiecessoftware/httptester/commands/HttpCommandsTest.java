package com.goldpiecessoftware.httptester.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpCommandsTest {

    @Test
    void get() {
        HttpCommands httpCommands = new HttpCommands();
        String result = httpCommands.get("https://www.google.com", 1,1);
        assertEquals("https://www.google.com", result);
    }
}