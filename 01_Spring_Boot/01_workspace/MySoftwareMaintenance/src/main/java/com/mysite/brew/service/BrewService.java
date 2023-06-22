package com.mysite.brew.service;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import com.google.gson.JsonParser;
import com.mysite.brew.model.BrewLs;
import com.mysite.brew.model.BrewOutdated;
import com.mysite.brew.model.BrewOutdatedPivot;
import com.mysite.brew.model.BrewUpdate;
import com.mysite.brew.repository.BrewLsRepository;
import com.mysite.brew.repository.BrewOutdatedPivotRepository;
import com.mysite.brew.repository.BrewOutdatedRepository;
import com.mysite.brew.repository.BrewUpdateRepository;
import com.mysite.brew.shell.TerminalStreamCallable;

import groovyjarjarantlr4.v4.parse.ANTLRParser.finallyClause_return;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BrewService {
    private final BrewUpdateRepository brewUpdateRepository;
    private final BrewOutdatedRepository brewOutdatedRepository;
    private final BrewOutdatedPivotRepository brewOutdatedPivotRepository;
    private final BrewLsRepository brewLsRepository;

    public void ls() throws IOException, InterruptedException, ExecutionException {
        // run ls
        List<String> resultList = lsRunByProcessBuilder("/home/linuxbrew/.linuxbrew/bin/brew ls --formulae --version");
        String content = "";
        for (String myLine : resultList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("\n$", "");

        // write ls to table
        BrewLs brewLs = null;
        for (String line : content.split("\\r?\\n")) {
            String[] items = line.split(" ");
            if (items.length > 1) {
                brewLs = new BrewLs();
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
                String myCurrentVersion = myJsonObject.get("current_version").getAsString();
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

    private List<String> lsRunByProcessBuilder(String command)
            throws IOException, InterruptedException, ExecutionException {
        return brewCommandRunByProcessBuilder(command);
    }

    public void update() throws AWTException, IOException, InterruptedException, ExecutionException {
        // run update
        List<String> resultList = updateRunByProcessBuilder("/home/linuxbrew/.linuxbrew/bin/brew update");

        // read update from resultList
        BrewUpdate brewUpdate = new BrewUpdate();
        String content = "";
        for (String myLine : resultList) {
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

    public Page<BrewUpdate> getBrewUpdateList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return brewUpdateRepository.findAll(pageable);
    }

    private List<String> updateRunByProcessBuilder(String command)
            throws IOException, InterruptedException, ExecutionException {
        return brewCommandRunByProcessBuilder(command);
    }

    private List<String> brewCommandRunByProcessBuilder(String command)
            throws IOException, InterruptedException, ExecutionException {
        return runByProcessBuilder(command);
    }

    private List<String> runByProcessBuilder(String command)
            throws IOException, InterruptedException, ExecutionException {
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
        TerminalStreamCallable terminalExecutor = new TerminalStreamCallable(process.getInputStream(),
                new ArrayList<String>());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<String>> listFuture = executorService.submit(terminalExecutor);

        int exitCode = process.waitFor(); /* 0 is normal termination */
        System.out.println(exitCode + " = exitCode : 0 is normal termination");

        List<String> terminalOutput = listFuture.get(); // wait untill terminalExecutor is finished
        for (String myTerminalLine : terminalOutput) {
            System.out.println(myTerminalLine);
        }
        return terminalOutput;
    }

    public void outdated() throws Exception {
        // run outdated
        List<String> resultList = outdatedRunByProcessBuilder("/home/linuxbrew/.linuxbrew/bin/brew outdated --json");

        // read outdated from resultList
        BrewOutdated brewOutdated = new BrewOutdated();
        String content = "";
        for (String myLine : resultList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("conten\n$", "");
        JsonObject jsonObject = (JsonObject) readJSON(content);
        JsonArray formulae = (JsonArray) jsonObject.get("formulae");
        Gson gson = new GsonBuilder().create();
        String formulaeString = gson.toJson(formulae);
        brewOutdated.getProperties().put("formulae", formulaeString);

        // write outdated to table
        this.brewOutdatedRepository.save(brewOutdated);
    }

    private List<String> outdatedRunByProcessBuilder(String command)
            throws IOException, InterruptedException, ExecutionException {
        return brewCommandRunByProcessBuilder(command);
    }

    private Object readJSON(String path) {
        Object result = null;
        if (path.charAt(0) == '{') {
            JsonObject jsonObject = new JsonParser().parse(path).getAsJsonObject();
            result = jsonObject;
        } else if (path.charAt(0) == '[') {
            JsonArray jsonArray = new JsonParser().parse(path).getAsJsonArray();
            result = jsonArray;
        }
        return result;
    }

    public Page<BrewOutdated> getBrewOutdatedList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<BrewOutdated> result = brewOutdatedRepository.findAll(pageable);
        return result;
    }

    public void upgrade(String name) throws IOException, InterruptedException, ExecutionException {
        String command = String.format("/home/linuxbrew/.linuxbrew/bin/brew upgrade %s", name);
        brewCommandRunByProcessBuilder(command);
    }

    public void cleanup() throws IOException, InterruptedException, ExecutionException {
        String command = "/home/linuxbrew/.linuxbrew/bin/brew cleanup";
        brewCommandRunByProcessBuilder(command);
    }

    public void outdatedPivot() {
        List<BrewOutdated> brewOutdatedList = this.brewOutdatedRepository.findAll();
        for (BrewOutdated myBrewOutdated : brewOutdatedList) {
            Map<String, String> myProperties = myBrewOutdated.getProperties();
            String myContent = myProperties.get("formulae");
            JsonArray jsonArray = (JsonArray) readJSON(myContent);
            jsonArray.forEach(myJson -> {
                System.out.println(myJson);
                String myName = myJson.getAsJsonObject().get("name").getAsString();
                String myInstalledVersion = myJson.getAsJsonObject().get("installed_versions").getAsString();
                String myCurrentVersion = myJson.getAsJsonObject().get("current_version").getAsString();
                Boolean myPinned = myJson.getAsJsonObject().get("pinned").getAsBoolean();
                BrewOutdatedPivot brewOutdatedPivot = new BrewOutdatedPivot();
                brewOutdatedPivot.setName(myName);
                brewOutdatedPivot.setInstalledVersion(myInstalledVersion);
                brewOutdatedPivot.setCurrentVersion(myCurrentVersion);
                brewOutdatedPivot.setPinned(myPinned);
                this.brewOutdatedPivotRepository.save(brewOutdatedPivot);
            });
        }
    }

    public Page<BrewOutdatedPivot> getBrewOutdatedPivotList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("lastModifiedDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<BrewOutdatedPivot> result = brewOutdatedPivotRepository.findAll(pageable);
        return result;
    }
}
