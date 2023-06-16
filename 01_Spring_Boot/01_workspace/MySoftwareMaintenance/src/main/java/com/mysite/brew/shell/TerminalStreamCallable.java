package com.mysite.brew.shell;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class TerminalStreamCallable implements Callable<List<String>> {
    private InputStream inputStream;
    private List<String> lines;

    public TerminalStreamCallable(InputStream inputStream, List<String> lines) {
        this.inputStream = inputStream;
        this.lines = lines;
    }

    @Override
    public List<String> call() throws Exception {
        new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(line -> {
            System.out.println(line);
            this.lines.add(line);
        });
        return this.lines;
    }
}
