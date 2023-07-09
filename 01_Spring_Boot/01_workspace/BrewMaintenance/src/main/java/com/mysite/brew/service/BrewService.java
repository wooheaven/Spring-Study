package com.mysite.brew.service;

import com.mysite.brew.model.BrewUpdate;
import com.mysite.brew.repository.BrewUpdateRepository;
import com.mysite.brew.shell.TerminalStreamCallable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Service
public class BrewService {
    private static final String brewUpdate = "/home/linuxbrew/.linuxbrew/bin/brew update";
    private final BrewUpdateRepository brewUpdateRepository;

    public void update() throws Exception {
        // run update
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = runByProcessBuilder(brewUpdate);
        }

        // read update from resultList
        BrewUpdate brewUpdate = new BrewUpdate();
        String content = "";
        for (String myLine : lineList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("\n$", "");

        // write update to table
        brewUpdate.setContent(content);
        this.brewUpdateRepository.save(brewUpdate);
    }

    private List<String> runByProcessBuilder(String command) throws Exception {
        boolean isLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");
        ProcessBuilder builder = new ProcessBuilder();
        if (isLinux) {
            builder.command("/bin/bash", "-c",
                    "eval \"$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)\" && " + command);
            builder.directory(new File("/home/linuxbrew"));
        } else {
            builder.command("bash.exe", "-c", command);
        }
        Process process = builder.start();
        List<String> terminalOutput = new ArrayList<>();
        TerminalStreamCallable terminalExecutor = new TerminalStreamCallable(process.getInputStream(), terminalOutput);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<String>> listFuture = executorService.submit(terminalExecutor);

        int exitCode = process.waitFor(); /* 0 is normal termination */
        System.out.println(exitCode + " = exitCode : 0 is normal termination");

        List<String> result = listFuture.get(); /* wait until terminalExecutor is finished */
        return result;
    }

    public Page<BrewUpdate> getBrewUpdateList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return brewUpdateRepository.findAll(pageable);
    }
}
