package com.mysite.brew.service;

import com.google.gson.*;
import com.mysite.brew.entity.BrewDeps;
import com.mysite.brew.entity.BrewOutdated;
import com.mysite.brew.entity.BrewOutdatedPivot;
import com.mysite.brew.entity.BrewUpdate;
import com.mysite.brew.repository.BrewDepsRepository;
import com.mysite.brew.repository.BrewOutdatedPivotRepository;
import com.mysite.brew.repository.BrewOutdatedRepository;
import com.mysite.brew.repository.BrewUpdateRepository;
import com.mysite.brew.shell.TerminalStreamCallable;
import com.mysite.common.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class BrewService {
    private static final String brewUpdate = "/home/linuxbrew/.linuxbrew/bin/brew update";
    private static final String brewOutdated = "/home/linuxbrew/.linuxbrew/bin/brew outdated --json";
    private static final String brewDeps = "/home/linuxbrew/.linuxbrew/bin/brew deps --graph --dot ";
    private static final String brewUpgrade = "/home/linuxbrew/.linuxbrew/bin/brew upgrade ";
    private static final String brewClean = "/home/linuxbrew/.linuxbrew/bin/brew cleanup";
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final BrewUpdateRepository brewUpdateRepository;
    private final BrewOutdatedRepository brewOutdatedRepository;
    private final BrewOutdatedPivotRepository brewOutdatedPivotRepository;
    private final BrewDepsRepository brewDepsRepository;
    private final CommonService commonService;

    @Autowired
    public BrewService(BrewUpdateRepository brewUpdateRepository, BrewOutdatedRepository brewOutdatedRepository,
                       BrewOutdatedPivotRepository brewOutdatedPivotRepository,
                       BrewDepsRepository brewDepsRepository, CommonService commonService) {
        this.brewUpdateRepository = brewUpdateRepository;
        this.brewOutdatedRepository = brewOutdatedRepository;
        this.brewOutdatedPivotRepository = brewOutdatedPivotRepository;
        this.brewDepsRepository = brewDepsRepository;
        this.commonService = commonService;
    }

    public void update() throws Exception {
        // run update
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = this.commonService.runByProcessBuilder(brewUpdate);
        }

        // read update
        String content = "";
        for (String myLine : lineList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("\n$", "");

        // write update to table
        BrewUpdate brewUpdate = new BrewUpdate();
        brewUpdate.setContent(content);
        this.brewUpdateRepository.save(brewUpdate);
    }

    public Page<BrewUpdate> getBrewUpdateList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.brewUpdateRepository.findAll(pageable);
    }

    public void outdated() throws Exception {
        // run outdated
        List<String> lineList = this.commonService.runByProcessBuilder(brewOutdated);

        // read outdated
        BrewOutdated brewOutdated = new BrewOutdated();
        String content = "";
        for (String myLine : lineList) {
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

    public Object readJSON(String content) {
        Object result = null;
        if (content.charAt(0) == '{') {
            JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
            result = jsonObject;
        } else if (content.charAt(0) == '[') {
            JsonArray jsonArray = new JsonParser().parse(content).getAsJsonArray();
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

    public void outdatedPivot() {
        this.brewOutdatedPivotRepository.deleteAll();
        BrewOutdated myBrewOutdated = this.brewOutdatedRepository.findFirstByOrderByIdDesc();
        Map<String, String> myProperties = myBrewOutdated.getProperties();
        String myContent = myProperties.get("formulae");
        JsonArray jsonArray = (JsonArray) readJSON(myContent);
        jsonArray.forEach(myJson -> {
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

    public void deps() throws Exception {
        this.brewDepsRepository.deleteAll();
        List<BrewOutdatedPivot> brewOutdatedPivotList = this.brewOutdatedPivotRepository.findAll();
        // insert BrewDeps from BrewOutdatedPivot
        for (BrewOutdatedPivot brewOutdatedPivot : brewOutdatedPivotList) {
            String myRootNode = brewOutdatedPivot.getName();
            if (myRootNode.equals("go")) {
                System.out.println("test");
            }
            // run deps
            List<String> resultList = this.commonService.runByProcessBuilder(brewDeps + myRootNode);

            // read deps from resultList
            if (resultList.get(0).equals("digraph {")) {
                resultList.remove(0);
            }
            if (resultList.get(resultList.size()-1).equals("}")) {
                resultList.remove(resultList.size()-1);
            }
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
                    if (mySplits.length != 2) {
                        System.out.println("test");
                    }
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

    public Page<BrewDeps> getBrewDepsList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("sortNumber"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        Page<BrewDeps> result = brewDepsRepository.findAll(pageable);
        return result;
    }

    public void upgrade(String name) throws Exception {
        this.commonService.runByProcessBuilder(brewUpgrade + name);
    }

    public void cleanup() throws Exception {
        this.commonService.runByProcessBuilder(brewClean);
    }
}
