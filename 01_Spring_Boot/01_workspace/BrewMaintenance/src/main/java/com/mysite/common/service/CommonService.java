package com.mysite.common.service;

import com.mysite.brew.shell.TerminalStreamCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class CommonService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    public List<String> runByProcessBuilder(String command) throws Exception {
        boolean isLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");
        ProcessBuilder builder = new ProcessBuilder();
        if (isLinux) {
            builder.command("/bin/bash", "-c",
                    "eval \"$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)\" && " + command);
            builder.directory(new File("/home/linuxbrew"));
        } else {
            builder.command("C:\\Windows\\System32\\bash.exe", "-c", command);
        }
        Process process = builder.start();
        List<String> terminalOutput = new ArrayList<>();
        TerminalStreamCallable terminalExecutor = new TerminalStreamCallable(process.getInputStream(), terminalOutput);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<String>> listFuture = executorService.submit(terminalExecutor);

        log.info(String.format("command is executed : %s", command));
        int exitCode = process.waitFor(); /* 0 is normal termination */
        log.info(String.format("%s = exitCode : 0 is normal termination", exitCode));

        List<String> result = listFuture.get(); /* wait until terminalExecutor is finished */
        return result;
    }
}
