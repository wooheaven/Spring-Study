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
    private static final String brewInit = "PATH=\"/home/linuxbrew/.linuxbrew/bin:${PATH}\" && ";
    private static final String brewUpdate = brewInit + "brew update";
    private static final String brewOutdated = brewInit + "brew outdated --json";
    private static final String brewDeps = brewInit + "brew deps --graph --dot ";
    private static final String brewUpgrade = brewInit + "brew upgrade ";
    private static final String brewClean = brewInit + "brew cleanup";
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
            // run deps
            List<String> resultList = this.commonService.getLineListByTerminalOut(brewDeps + myRootNode);

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
        List<BrewDeps> brewDepsList = brewDepsRepository.updateSortNumberByCustom(rootNodeList);
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

    public void cleanup() throws Exception {
        this.commonService.getLineListByTerminalOut(brewClean);
    }
}
