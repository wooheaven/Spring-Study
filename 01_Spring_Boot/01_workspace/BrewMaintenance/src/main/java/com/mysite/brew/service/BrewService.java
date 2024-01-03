package com.mysite.brew.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysite.brew.entity.*;
import com.mysite.brew.repository.*;
import com.mysite.common.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BrewService {
    private static final String brewInit = "export PATH='/home/linuxbrew/.linuxbrew/bin:/home/linuxbrew/.linuxbrew/sbin:/bin:/usr/bin:${PATH}' && ";
    private static final String brewUpdate = brewInit + "brew update 2>&1 ";
    private static final String brewOutdated = brewInit + "brew outdated --json | jq -M ";
    private static final String brewDeps = brewInit + "brew deps --graph --dot ";
    private static final String brewDepsInstalled = brewInit + "brew deps --tree --installed ";
    private static final String brewUpgrade = brewInit + "brew upgrade ";
    private static final String brewAutoremove = brewInit + "brew autoremove 2>&1 ";
    private static final String brewClean = brewInit + "brew cleanup 2>&1 ";
    private static final String brewDoctor = brewInit + "brew doctor 2>&1 ";
    private static final String brewList = brewInit + "brew list --version 2>&1 ";
    private static final String brewUsesInstalled = brewInit + "brew uses --installed ";
    private static final String brewInfoJson = brewInit + "brew info --json ";
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final BrewUpdateRepository brewUpdateRepository;
    private final BrewOutdatedRepository brewOutdatedRepository;
    private final BrewOutdatedPivotRepository brewOutdatedPivotRepository;
    private final BrewDepsRepository brewDepsRepository;
    private final BrewCleanRepository brewCleanRepository;
    private final BrewDoctorRepository brewDoctorRepository;
    private final BrewListRepository brewListRepository;
    private final BrewDepsInstalledRepository brewDepsInstalledRepository;
    private final BrewUsesRepository brewUsesRepository;
    private final BrewInfoRepository brewInfoRepository;
    private final BrewAutoremoveRepository brewAutoremoveRepository;
    private final CommonService commonService;

    @Autowired
    public BrewService(BrewUpdateRepository brewUpdateRepository, BrewOutdatedRepository brewOutdatedRepository,
                       BrewOutdatedPivotRepository brewOutdatedPivotRepository,
                       BrewDepsRepository brewDepsRepository,
                       BrewCleanRepository brewCleanRepository,
                       BrewDoctorRepository brewDoctorRepository,
                       BrewListRepository brewListRepository,
                       BrewDepsInstalledRepository brewDepsInstalledRepository,
                       BrewUsesRepository brewUsesRepository,
                       BrewInfoRepository brewInfoRepository,
                       BrewAutoremoveRepository brewAutoremoveRepository,
                       CommonService commonService) {
        this.brewUpdateRepository = brewUpdateRepository;
        this.brewOutdatedRepository = brewOutdatedRepository;
        this.brewOutdatedPivotRepository = brewOutdatedPivotRepository;
        this.brewDepsRepository = brewDepsRepository;
        this.brewCleanRepository = brewCleanRepository;
        this.brewDoctorRepository = brewDoctorRepository;
        this.brewListRepository = brewListRepository;
        this.brewDepsInstalledRepository = brewDepsInstalledRepository;
        this.brewUsesRepository = brewUsesRepository;
        this.brewInfoRepository = brewInfoRepository;
        this.brewAutoremoveRepository = brewAutoremoveRepository;
        this.commonService = commonService;
    }

    public void update() throws Exception {
        // run update
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = this.commonService.getLineListByTerminalOut(brewUpdate);
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
        List<String> lineList = this.commonService.getLineListByTerminalOut(brewOutdated);

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
        JsonObject jsonObject = (JsonObject) commonService.readJSON(content);
        JsonArray formulae = (JsonArray) jsonObject.get("formulae");
        Gson gson = new GsonBuilder().create();
        String formulaeString = gson.toJson(formulae);
        brewOutdated.getProperties().put("formulae", formulaeString);

        // write outdated to table
        this.brewOutdatedRepository.save(brewOutdated);
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
        JsonArray jsonArray = (JsonArray) commonService.readJSON(myContent);
        jsonArray.forEach(myJson -> {
            String myName = myJson.getAsJsonObject().get("name").getAsString();
            String myInstalledVersion = jsonArrayToString((JsonArray) myJson.getAsJsonObject().get("installed_versions"));
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

    private String jsonArrayToString(JsonArray myJsonArray) {
        final String[] result = {""};
        myJsonArray.forEach(myVersionJson -> {
            result[0] += myVersionJson.getAsString();
            result[0] += ",";
        });
        result[0] = result[0].replaceAll(",$", "");
        return result[0];
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
            // run deps
            List<String> resultList = this.commonService.getLineListByTerminalOut(brewDeps + myRootNode);

            // read deps from resultList
            if (resultList.get(0).equals("digraph {")) {
                resultList.remove(0);
            }
            if (resultList.get(resultList.size() - 1).equals("}")) {
                resultList.remove(resultList.size() - 1);
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
        rootNodeList = sortByDependency(rootNodeList);
        List<BrewDeps> brewDepsList = brewDepsRepository.customUpdateSortNumber(rootNodeList);
        for (BrewDeps myBrewDeps : brewDepsList) {
            this.brewDepsRepository.save(myBrewDeps);
        }
    }

    private List<String> sortByDependency(List<String> rootNodeList) {
        List<String> result = null;
        if (1 == rootNodeList.size()) {
            result = rootNodeList;
        } else {
            int rootNodeListSize = rootNodeList.size();
            for (int i = 0; i < rootNodeListSize - 2; i++) {
                for (int j = i + 1; j < rootNodeListSize - 1; j++) {
                    String iRootNode = rootNodeList.get(i);
                    String jRootNode = rootNodeList.get(j);
                    Integer count = this.brewDepsRepository.countByRootNodeAndChildNode(iRootNode, jRootNode);
                    if (0 < count) {
                        rootNodeList.remove(j);
                        rootNodeList.add(i, jRootNode);
                        rootNodeList = sortByDependency(rootNodeList);
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
        this.commonService.getLineListByTerminalOut(brewUpgrade + name);
    }

    public void delete(String rootNode) {
        List<BrewDeps> brewDepsList = this.brewDepsRepository.findAllByRootNode(rootNode);
        for (BrewDeps myBrewDeps : brewDepsList) {
            this.brewDepsRepository.delete(myBrewDeps);
        }
    }

    public void autoremove() throws Exception {
        // run brew autoremove
        List<String> lineList = this.commonService.getLineListByTerminalOut(brewAutoremove);

        // read brew autoremove
        String content = "";
        for (String myLine : lineList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("\n$", "");

        // write update to table
        BrewAutoremove brewAutoremove = new BrewAutoremove();
        brewAutoremove.setContent(content);
        this.brewAutoremoveRepository.save(brewAutoremove);
    }

    public Page<BrewAutoremove> getBrewAutoremoveList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        Page<BrewAutoremove> result = brewAutoremoveRepository.findAll(pageable);
        return result;
    }

    public void cleanup() throws Exception {
        // run brew clean
        List<String> lineList = this.commonService.getLineListByTerminalOut(brewClean);

        // read brew clean
        String content = "";
        for (String myLine : lineList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("\n$", "");

        // write update to table
        BrewClean brewClean = new BrewClean();
        brewClean.setContent(content);
        this.brewCleanRepository.save(brewClean);
    }

    public Page<BrewClean> getBrewCleanupList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<BrewClean> result = brewCleanRepository.findAll(pageable);
        return result;
    }

    public void doctor() throws Exception {
        // run brew doctor
        List<String> lineList = this.commonService.getLineListByTerminalOut(brewDoctor);

        // read brew doctor
        String content = "";
        for (String myLine : lineList) {
            content += myLine;
            content += "\n";
        }
        content = content.replaceAll("\n$", "");

        // write update to table
        BrewDoctor brewDoctor = new BrewDoctor();
        brewDoctor.setContent(content);
        this.brewDoctorRepository.save(brewDoctor);
    }

    public Page<BrewDoctor> getBrewDoctorList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<BrewDoctor> result = brewDoctorRepository.findAll(pageable);
        return result;
    }

    public void list() throws Exception {
        // run brew list
        List<String> lineList = this.commonService.getLineListByTerminalOut(brewList);

        // read brew list
        List<BrewList> brewLists = new ArrayList<>();
        for (String myLine : lineList) {
            String[] myData = myLine.split(" ");
            String myName = myData[0];
            String myVersion = myData[1];
            BrewList brewList = new BrewList();
            brewList.setName(myName);
            brewList.setVersion(myVersion);
            brewLists.add(brewList);
        }

        // write brew list
        this.brewListRepository.saveAll(brewLists);
    }

    public Page<BrewList> getBrewListLast(int page) {
        String lastName = brewListRepository.findLastName();
        Long secondID = brewListRepository.customFindSecondIDByLastName(lastName);

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("brew_list_id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        Page<BrewList> result = brewListRepository.findAllByLast(secondID, pageable);
        return result;
    }

    public Page<BrewList> getBrewListAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("brew_list_id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        Page<BrewList> result = brewListRepository.findAllByLast(0L, pageable);
        return result;
    }

    public void depsInstalled(String name) throws Exception {
        // run brew deps --tree --installed
        List<String> lineList = this.commonService.getLineListByTerminalOut(brewDepsInstalled + name);

        // read brew deps --tree --installed
        String content = "";
        for (String myLine : lineList) {
            content += myLine;
            content += "\n";
        }
        content = content.replaceAll("\n$", "");

        // write brew deps --tree --installed
        BrewDepsInstalled brewDepsInstalled = new BrewDepsInstalled();
        brewDepsInstalled.setContent(content);
        this.brewDepsInstalledRepository.save(brewDepsInstalled);
    }

    public Page<BrewDepsInstalled> getBrewDepsInstalled(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        Page<BrewDepsInstalled> result = brewDepsInstalledRepository.findAll(pageable);
        return result;
    }

    public void usesInstalled(String name) throws Exception {
        // run brew uses --installed
        List<String> lineList = this.commonService.getLineListByTerminalOut(brewUsesInstalled + name);

        // read brew uses --installed
        String content = "";
        for (String myLine : lineList) {
            content += myLine;
            content += "\n";
        }
        content = content.replaceAll("\n$", "");

        // write brew uses --installed
        BrewUsesInstalled brewUsesInstalled = new BrewUsesInstalled();
        brewUsesInstalled.setName(name);
        brewUsesInstalled.setDeps(content);
        this.brewUsesRepository.save(brewUsesInstalled);
    }

    public Page<BrewUsesInstalled> getBrewUsesInstalled(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        Page<BrewUsesInstalled> result = brewUsesRepository.findAll(pageable);
        return result;
    }

    public void info(String name) throws Exception {
        // run brew info --json
        List<String> lineList = this.commonService.getLineListByTerminalOut(brewInfoJson + name);

        // read brew info --json
        String content = "";
        for (String myLine : lineList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("\n$", "");

        // save brew info --json
        BrewInfo brewInfo = new BrewInfo();
        brewInfo.setContent(content);
        this.brewInfoRepository.save(brewInfo);
    }

    public Page<BrewInfo> getBrewInfo(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        Page<BrewInfo> result = brewInfoRepository.findAll(pageable);
        return result;
    }

}
