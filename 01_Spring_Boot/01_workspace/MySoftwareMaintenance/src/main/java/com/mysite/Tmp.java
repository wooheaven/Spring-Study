package com.mysite;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.mysite.brew.shell.StreamGobble;

public class Tmp {

    public void db() throws IOException, InterruptedException {
        String command = "/home/woo/02_Documents/50_Local_PostgreSQL_WorkSpace/04_pg15.1-Container_start.sh";
        brewCommandRunByProcessBuilder(command);
    }

    private void brewCommandRunByProcessBuilder(String command) throws IOException, InterruptedException {
        runByProcessBuilder("/home/linuxbrew/", command);
    }

    private void runByProcessBuilder(String workingDirectory, String command) throws IOException, InterruptedException {
        boolean isLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");
        ProcessBuilder builder = new ProcessBuilder();
        if (isLinux) {
            String fullCommand = " && " + "cd " + workingDirectory + " && " + command;
            builder.command("/bin/bash", "-c", "eval \"$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)\"" + fullCommand);
        } else {
            builder.command("cmd.exe", "/c", "dir");
        }
        builder.directory(new File("/home/linuxbrew"));
        Process process = builder.start();
        StreamGobble streamGobble = new StreamGobble(process.getInputStream(), System.out::println);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(streamGobble);

        int exitCode = process.waitFor(); /* 0 is normal termination */
        System.out.println(exitCode + " = exitCode : 0 is normal termination");
    }
}
