package com.mysite.brew.service;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysite.brew.model.BrewOutdated;
import com.mysite.brew.model.BrewUpdate;
import com.mysite.brew.repository.BrewOutdatedRepository;
import com.mysite.brew.repository.BrewUpdateRepository;
import com.mysite.brew.shell.StreamGobbler;
import com.mysite.robot.MyRobot;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BrewService {
    private final BrewUpdateRepository brewUpdateRepository;
    private final MyRobot myRobot;
    private final BrewOutdatedRepository brewOutdatedRepository;

    public void update() throws AWTException, IOException, InterruptedException {
        // run update
        updateRunByProcessBuilder();
        String path = "/home/linuxbrew/00_brew_update.log";
        File f = new File(path);
        while (!f.exists()) {
            System.out.println(path + " file is not existed. So Wait 1 second");
            TimeUnit.SECONDS.sleep(1);
        }

        // read update from log file
        BrewUpdate brewUpdate = new BrewUpdate();
        String content = readFile(path);
        if ("" != content) {
            f.delete();
        }

        // write update to table
        brewUpdate.setContent(content);
        LocalDateTime localDateTime = LocalDateTime.now();
        brewUpdate.setCreateTime(localDateTime);
        brewUpdate.setModifyTime(localDateTime);
        this.brewUpdateRepository.save(brewUpdate);
    }

    public Page<BrewUpdate> getBrewUpdateList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return brewUpdateRepository.findAll(pageable);
    }

    private void updateRunByProcessBuilder() throws IOException, InterruptedException {
        RunByProcessBuilder("/home/linuxbrew/00_brew_update.sh");
    }

    private void RunByProcessBuilder(String cmd) throws IOException, InterruptedException {
        boolean isLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");
        ProcessBuilder builder = new ProcessBuilder();
        if (isLinux) {
            builder.command("/bin/bash", "-c", "eval \"$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)\" && " + cmd);
        } else {
            builder.command("cmd.exe", "/c", "dir");
        }
        builder.directory(new File("/home/linuxbrew"));
        Process process = builder.start();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(streamGobbler);

        int exitCode = process.waitFor(); /* 0 is normal termination */
        System.out.println(exitCode + " = exitCode : 0 is normal termination");
    }

    private String readFile(String path) throws IOException {
        Path filePath = Path.of(path);
        String content = Files.readString(filePath);
        return content;
    }

    public void outdated() throws Exception {
        // run outdated
        outdatedRunByProcessBuilder();
        String path = "/home/linuxbrew/01_brew_outdated.json";
        File f = new File(path);
        while (!f.exists()) {
            System.out.println(path + " file is not existed. So Wait 1 second");
            TimeUnit.SECONDS.sleep(1);
        }

        // read outdated from json file
        BrewOutdated brewOutdated = new BrewOutdated();
        JsonObject jsonObject = readJSON("/home/linuxbrew/01_brew_outdated.json");
        if (null != jsonObject) {
            f.delete();
        }
        JsonArray formulae = (JsonArray) jsonObject.get("formulae");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        brewOutdated.getProperties().put("formulae", gson.toJson(formulae));
        LocalDateTime localDateTime = LocalDateTime.now();
        brewOutdated.setCreateTime(localDateTime);
        brewOutdated.setModifyTime(localDateTime);

        // write outdated to table
        this.brewOutdatedRepository.save(brewOutdated);
    }

    private void outdatedRunByProcessBuilder() throws IOException, InterruptedException {
        RunByProcessBuilder("/home/linuxbrew/01_brew_outdated.sh");
    }

    private JsonObject readJSON(String path) {
        JsonObject jsonObject = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            Gson gson = new Gson();
            jsonObject = gson.fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public Page<BrewOutdated> getBrewOutdatedList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return brewOutdatedRepository.findAll(pageable);
    }
}
