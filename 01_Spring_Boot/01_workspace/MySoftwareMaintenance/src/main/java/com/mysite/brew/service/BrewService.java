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
import java.util.Map;
import java.util.Optional;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysite.brew.model.BrewLs;
import com.mysite.brew.model.BrewOutdated;
import com.mysite.brew.model.BrewUpdate;
import com.mysite.brew.repository.BrewLsRepository;
import com.mysite.brew.repository.BrewOutdatedRepository;
import com.mysite.brew.repository.BrewUpdateRepository;
import com.mysite.brew.shell.StreamGobbler;
import com.mysite.robot.MyRobot;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BrewService {
    private final BrewUpdateRepository brewUpdateRepository;
    private final BrewOutdatedRepository brewOutdatedRepository;
    private final BrewLsRepository brewLsRepository;
    private final MyRobot myRobot;

    public void ls() throws IOException, InterruptedException {
        // run ls
        String path = "/home/linuxbrew/01_brew_ls.log";
        lsRunByProcessBuilder(path);
        File f = new File(path);
        while (!f.exists()) {
            System.out.println(path + " file is not existed. So Wait 1 second");
            TimeUnit.SECONDS.sleep(1);
        }

        // read update from log file
        String content = readFile(path);
        if ("" != content) {
            f.delete();
        }

        // write ls to table
        LocalDateTime localDateTime = LocalDateTime.now();
        BrewLs brewLs = null;
        for (String line : content.split("\\r?\\n")) {
            String[] items = line.split(" ");
            if (items.length > 1) {
                brewLs = new BrewLs();
                brewLs.setCreateTime(localDateTime);
                brewLs.setModifyTime(localDateTime);
                brewLs.setPackageName(items[0]);
                brewLs.setInstalledVersion(items[1]);
                List<BrewLs> idList = this.brewLsRepository.findAllByPackageNameOrderById(brewLs.getPackageName());
                if (idList.size() > 0) {
                    brewLs.setId(idList.get(0).getId());
                }
                this.brewLsRepository.save(brewLs);
            }
        }
    }

    public Page<BrewLs> getBrewLsList(int page) {
        BrewOutdated brewOutdated = brewOutdatedRepository.findFirstByOrderByIdDesc();
        Gson gson = new GsonBuilder().create();
        Map<String, String> myProperties = brewOutdated.getProperties();
        String myValueString = myProperties.get("formulae");
        JsonArray myValueJsonArray = gson.fromJson(myValueString, JsonArray.class);
        if (myValueJsonArray.size() > 0) {
            for (JsonElement myJson : myValueJsonArray) {
                JsonObject myJsonObject = myJson.getAsJsonObject();
                String myName = myJsonObject.get("name").getAsString();
                String myInstalledVersion = gson.toJson(myJsonObject.get("installed_versions").getAsJsonArray().get(0));
                String myCurrentVersion = myJsonObject.get("current_version").getAsString();
                String myPinned = myJsonObject.get("pinned").getAsString();
                BrewLs myBrewLs = brewLsRepository.findByPackageName(myName);
                myBrewLs.setPackageName(myName);
                myBrewLs.setCurrentVersion(myCurrentVersion);
                brewLsRepository.save(myBrewLs);
            }
        }

        List<Map<String, Long>> idMapToSortNumber = brewLsRepository.findIdSortNumberByCustom();
        if (idMapToSortNumber.size() > 0) {
            for (Map<String, Long> myMap : idMapToSortNumber) {
                Long myId = myMap.get("brew_ls_id");
                Optional<BrewLs> myBrewLs = brewLsRepository.findById(myId);
                if (myBrewLs.isPresent()) {
                    Long mySortNumberLong = myMap.get("sort_number");
                    myBrewLs.get().setSortNumber(mySortNumberLong);
                    brewLsRepository.save(myBrewLs.get());
                }
            }
        }
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("sortNumber"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<BrewLs> result = brewLsRepository.findAll(pageable);
        return result;
    }

    private void lsRunByProcessBuilder(String path) throws IOException, InterruptedException {
        String command = path.replaceFirst("log", "sh");
        brewCommandRunByProcessBuilder(command);
    }

    public void update() throws AWTException, IOException, InterruptedException {
        // run update
        String path = "/home/linuxbrew/02_brew_update.log";
        updateRunByProcessBuilder(path);
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

    private void updateRunByProcessBuilder(String path) throws IOException, InterruptedException {
        String command = path.replaceFirst("log", "sh");
        brewCommandRunByProcessBuilder(command);
    }

    private void runByProcessBuilder(String workingDirectory, String command) throws IOException, InterruptedException {
        boolean isLinux = System.getProperty("os.name").toLowerCase().startsWith("linux");
        ProcessBuilder builder = new ProcessBuilder();
        if (isLinux) {
            String fullCommand = " && " + "cd " + workingDirectory + " && " + command;
            builder.command("/bin/bash", "-c",
                    "eval \"$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)\"" + fullCommand);
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

    private void brewCommandRunByProcessBuilder(String command) throws IOException, InterruptedException {
        runByProcessBuilder("/home/linuxbrew/", command);
    }

    private String readFile(String path) throws IOException {
        Path filePath = Path.of(path);
        String content = Files.readString(filePath);
        return content;
    }

    public void outdated() throws Exception {
        // run outdated
        String path = "/home/linuxbrew/03_brew_outdated.json";
        outdatedRunByProcessBuilder(path);
        File f = new File(path);
        while (!f.exists()) {
            System.out.println(path + " file is not existed. So Wait 1 second");
            TimeUnit.SECONDS.sleep(1);
        }

        // read outdated from json file
        BrewOutdated brewOutdated = new BrewOutdated();
        JsonObject jsonObject = readJSON(path);
        if (null != jsonObject) {
            f.delete();
        }
        JsonArray formulae = (JsonArray) jsonObject.get("formulae");
        Gson gson = new GsonBuilder().create();
        String formulaeString = formulae.toString();
        formulaeString = gson.toJson(formulae);
        brewOutdated.getProperties().put("formulae", formulaeString);
        LocalDateTime localDateTime = LocalDateTime.now();
        brewOutdated.setCreateTime(localDateTime);
        brewOutdated.setModifyTime(localDateTime);

        // write outdated to table
        this.brewOutdatedRepository.save(brewOutdated);
    }

    private void outdatedRunByProcessBuilder(String path) throws IOException, InterruptedException {
        String command = path.replaceFirst("json", "sh");
        brewCommandRunByProcessBuilder(command);
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
        Page<BrewOutdated> result = brewOutdatedRepository.findAll(pageable);
        return result;
    }

    public void db() throws IOException, InterruptedException {
        String command = "/home/woo/02_Documents/50_Local_PostgreSQL_WorkSpace/04_pg15.1-Container_start.sh";
        brewCommandRunByProcessBuilder(command);
    }

    public void upgrade(String name) throws IOException, InterruptedException {
        String command = String.format("brew upgrade %s", name);
        brewCommandRunByProcessBuilder(command);
    }

    public void cleanup() throws IOException, InterruptedException {
        String command = "brew cleanup";
        brewCommandRunByProcessBuilder(command);
    }
}
