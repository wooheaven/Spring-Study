package com.mysite.brew.service;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysite.brew.model.BrewDeps;
import com.mysite.brew.model.BrewOutdated;
import com.mysite.brew.model.BrewOutdatedPivot;
import com.mysite.brew.model.BrewUpdate;
import com.mysite.brew.repository.BrewDepsRepository;
import com.mysite.brew.repository.BrewOutdatedPivotRepository;
import com.mysite.brew.repository.BrewOutdatedRepository;
import com.mysite.brew.repository.BrewUpdateRepository;
import com.mysite.brew.shell.TerminalStreamCallable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BrewService {
    private final BrewUpdateRepository brewUpdateRepository;
    private final BrewOutdatedRepository brewOutdatedRepository;
    private final BrewOutdatedPivotRepository brewOutdatedPivotRepository;
    private final BrewDepsRepository brewDepsRepository;

    public void update() throws AWTException, IOException, InterruptedException, ExecutionException {
        // run update
        List<String> resultList = new ArrayList<>();
        while (0 == resultList.size()) {
            resultList = updateRunByProcessBuilder("/home/linuxbrew/.linuxbrew/bin/brew update");
        }

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
        List<String> terminalOutput = new ArrayList<>();
        TerminalStreamCallable terminalExecutor = new TerminalStreamCallable(process.getInputStream(), terminalOutput);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<String>> listFuture = executorService.submit(terminalExecutor);

        int exitCode = process.waitFor(); /* 0 is normal termination */
        System.out.println(exitCode + " = exitCode : 0 is normal termination");

        List<String> result = listFuture.get(); // wait until terminalExecutor is finished
        return result;
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
        this.brewOutdatedPivotRepository.deleteAll();
        BrewOutdated myBrewOutdated = this.brewOutdatedRepository.findFirstByOrderByIdDesc();
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

    public Page<BrewOutdatedPivot> getBrewOutdatedPivotList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("lastModifiedDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<BrewOutdatedPivot> result = brewOutdatedPivotRepository.findAll(pageable);
        return result;
    }

    public void deps() throws IOException, InterruptedException, ExecutionException {
        this.brewDepsRepository.deleteAll();
        List<BrewOutdatedPivot> brewOutdatedPivotList = this.brewOutdatedPivotRepository.findAll();
        // insert BrewDeps from BrewOutdatedPivot
        for (BrewOutdatedPivot brewOutdatedPivot : brewOutdatedPivotList) {
            String myRootNode = brewOutdatedPivot.getName();
            // run deps
            List<String> resultList = depsRunByProcessBuilder(
                    "/home/linuxbrew/.linuxbrew/bin/brew deps --graph --dot " + myRootNode);

            // read deps from resultList
            String content = "";
            for (String myLine : resultList) {
                if (myLine.length() > 0) {
                    content += myLine;
                    content += "\n";
                }
            }
            content = content.replaceAll("\n$", "");
            content = content.replaceAll("  \"", "");
            content = content.replaceAll("\" -> \"", " ");
            content = content.replaceAll("\"", "");

            // save deps from content
            if (content.length() == 0) {
                BrewDeps brewDeps = new BrewDeps();
                brewDeps.setRootNode(myRootNode);
                brewDeps.setParentNode(myRootNode);
                brewDeps.setLeafNode(myRootNode);
                brewDeps.setLevel(1);
                this.brewDepsRepository.save(brewDeps);
            } else {
                for (String myLine : content.split("\n")) {
                    String[] mySplits = myLine.split(" ");
                    String myParentNode = mySplits[0];
                    String myChildNode = mySplits[1];
                    BrewDeps brewDeps = new BrewDeps();
                    brewDeps.setRootNode(myRootNode);
                    brewDeps.setParentNode(myParentNode);
                    brewDeps.setChildNode(myChildNode);
                    if (myRootNode.equals(myParentNode)) {
                        brewDeps.setLevel(1);
                    }
                    this.brewDepsRepository.save(brewDeps);
                }
            }

            // update level
            Integer count = this.brewDepsRepository.countByLevel(null);
            Integer myLevel = 1;
            while (0 < count) {
                List<BrewDeps> brewDepsList = this.brewDepsRepository.findAllByRootNodeAndLevel(myRootNode, myLevel);
                for (BrewDeps myBrewDeps : brewDepsList) {
                    String rootNode = myBrewDeps.getRootNode();
                    String parentNode = myBrewDeps.getParentNode();
                    String childNode = myBrewDeps.getChildNode();
                    List<BrewDeps> targetList = new ArrayList<BrewDeps>();
                    if (childNode.equals("python")) {
                        targetList = this.brewDepsRepository.findAllByRootNodeAndParentNodeStartsWithAndLevel(rootNode,
                                childNode, null);
                    } else {
                        targetList = this.brewDepsRepository.findAllByRootNodeAndParentNodeAndLevel(rootNode,
                                childNode, null);
                    }
                    for (BrewDeps targetBrewDeps : targetList) {
                        targetBrewDeps.setLevel(myLevel + 1);
                        if (targetBrewDeps.getChildNode().length() == 0) {
                            targetBrewDeps.setLeafNode(targetBrewDeps.getParentNode());
                        }
                        this.brewDepsRepository.save(targetBrewDeps);
                    }
                }
                myLevel += 1;
                count = this.brewDepsRepository.countByLevel(null);
            }
        }

        // update sortNumber
        List<String> rootNodeList = brewDepsRepository.findGroupByRootNodeWithCustom();
        rootNodeList = sortBydependency(rootNodeList);
        List<BrewDeps> brewDepsList = brewDepsRepository.updateSortNumberByCustom(rootNodeList);
        for (BrewDeps myBrewDeps : brewDepsList) {
            this.brewDepsRepository.save(myBrewDeps);
        }
    }

    private List<String> sortBydependency(List<String> rootNodeList) {
        List<String> result = null;
        if (1 == rootNodeList.size()) {
            result = rootNodeList;
        } else {
            int rootNodeListSize = rootNodeList.size();
            for (int i = 0; i < rootNodeListSize - 1; i++) {
                for (int j = i + 1; j < rootNodeListSize; j++) {
                    String iRootNode = rootNodeList.get(i);
                    String jRootNode = rootNodeList.get(j);
                    Integer count = this.brewDepsRepository.countByRootNodeAndChildNode(jRootNode, iRootNode);
                    if (0 < count) {
                        Collections.swap(rootNodeList, i, j);
                        rootNodeList = sortBydependency(rootNodeList);
                    }
                }
            }
            result = rootNodeList;
        }
        return result;
    }

    private List<String> depsRunByProcessBuilder(String command)
            throws IOException, InterruptedException, ExecutionException {
        List<String> result = brewCommandRunByProcessBuilder(command);
        int size = result.size();
        result.remove(size - 1);
        result.remove(0);
        return result;
    }

    public Page<BrewDeps> getBrewDepsList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("sortNumber"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        Page<BrewDeps> result = brewDepsRepository.findAll(pageable);
        return result;
    }

}
