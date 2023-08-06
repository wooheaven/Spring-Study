package com.mysite.sdk.service;

import com.google.gson.JsonArray;
import com.mysite.common.service.CommonService;
import com.mysite.sdk.entity.SdkCandidates;
import com.mysite.sdk.entity.SdkList;
import com.mysite.sdk.entity.SdkUpdate;
import com.mysite.sdk.entity.SdkVersion;
import com.mysite.sdk.repository.SdkCandidatesRepository;
import com.mysite.sdk.repository.SdkListRepository;
import com.mysite.sdk.repository.SdkUpdateRepository;
import com.mysite.sdk.repository.SdkVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SdkService {
    private static final String brewInit = "PATH=\"/home/linuxbrew/.linuxbrew/bin:${PATH}\" && ";
    private static final String sdkInit = "source $HOME/.sdkman/bin/sdkman-init.sh && ";
    private static final String sdkUpdate = sdkInit + "sdk update";
    private static final String sdkVersion = sdkInit + "sdk version";
    private static final String sdkList = sdkInit + "PAGER=cat sdk list %s | grep '|' | sed 's/ //g' | sed 's/|/,/g'";
    private static final String sdkCandidates = sdkInit + brewInit + "tree -J -d -L 2 ~/.sdkman/candidates/ | jq -M";
    private final CommonService commonService;
    private final SdkUpdateRepository sdkUpdateRepository;
    private final SdkVersionRepository sdkVersionRepository;
    private final SdkListRepository sdkListRepository;
    private final SdkCandidatesRepository sdkCandidatesRepository;

    @Autowired
    public SdkService(CommonService commonService,
                      SdkUpdateRepository sdkUpdateRepository,
                      SdkVersionRepository sdkVersionRepository,
                      SdkListRepository sdkListRepository,
                      SdkCandidatesRepository sdkCandidatesRepository) {
        this.commonService = commonService;
        this.sdkUpdateRepository = sdkUpdateRepository;
        this.sdkVersionRepository = sdkVersionRepository;
        this.sdkListRepository = sdkListRepository;
        this.sdkCandidatesRepository = sdkCandidatesRepository;
    }

    public void update() throws Exception {
        // run sdk update
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = this.commonService.getLineListByTerminalOut(sdkUpdate);
        }

        // read sdk update
        if (lineList.get(0).equals("\u001B[1;32m")) {
            lineList.remove(0);
        }
        int targetNum = lineList.size() - 1;
        String targetLine = lineList.get(targetNum);
        if (targetLine.contains("\u001B[0m")) {
            targetLine = targetLine.replace("\u001B[0m", "");
            lineList.remove(targetNum);
            lineList.add(targetLine);
        }
        String content = "";
        for (String myLine : lineList) {
            content += myLine;
            content += "\n";
        }
        content = content.replaceAll("\n$", "");

        // save sdk update from content
        SdkUpdate sdkUpdate = new SdkUpdate();
        sdkUpdate.setContent(content);
        this.sdkUpdateRepository.save(sdkUpdate);
    }

    public Page<SdkUpdate> getSdkUpdateList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.sdkUpdateRepository.findAll(pageable);
    }

    public void version() throws Exception {
        // run sdk version
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = this.commonService.getLineListByTerminalOut(sdkVersion);
        }

        // read sdk version
        if (lineList.get(0).equals("")) {
            lineList.remove(0);
        }
        if (lineList.get(lineList.size() - 1).equals("")) {
            lineList.remove(lineList.size() - 1);
        }
        String content = "";
        for (String myLine : lineList) {
            content += myLine;
            content += "\n";
        }
        content = content.replaceAll("\n$", "");

        // save sdk version from content
        SdkVersion sdkVersion = new SdkVersion();
        sdkVersion.setContent(content);
        this.sdkVersionRepository.save(sdkVersion);
    }

    public Page<SdkVersion> getSdkVersionList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.sdkVersionRepository.findAll(pageable);
    }

    public void list(String name) throws Exception {
        // delete sdk list
        this.sdkListRepository.deleteAll();

        // run sdk list
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            String cmd = String.format(sdkList, name);
            lineList = this.commonService.getLineListByTerminalOut(cmd);
        }

        // read sdk list
        String content = "";
        String myVendor = "";
        for (String myLine : lineList) {
            if (myLine.length() > 0) {
                String[] fields = myLine.split(",");
                if (fields[0].length() > 0) {
                    myVendor = fields[0];
                } else {
                    fields[0] = myVendor;
                    myLine = String.join(",", fields);
                }
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("\n$", "");
        content = content.replaceAll(">>>", "current");

        // save sdk version from content
        for (String myLine : content.split("\n")) {
            if (!myLine.contains("Vendor")) {
                String[] fields = myLine.split(",");
                SdkList sdkList = new SdkList();
                sdkList.setLib("java");
                sdkList.setVendor(fields[0]);
                sdkList.setUse(fields[1]);
                sdkList.setVersion(fields[2]);
                sdkList.setDist(fields[3]);
                sdkList.setStatus(fields[4]);
                sdkList.setIdentifier(fields[5]);
                this.sdkListRepository.save(sdkList);
            }
        }
    }

    public Page<SdkList> getSdkList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        return this.sdkListRepository.findAll(pageable);
    }

    public void getSdkCandidates() throws Exception {
        // run sdk candidates
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            String cmd = String.format(sdkCandidates);
            lineList = this.commonService.getLineListByTerminalOut(cmd);
        }

        // read sdk candidates
        String content = "";
        for (String myLine : lineList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("\n$", "");
        JsonArray jsonArray = (JsonArray) commonService.readJSON(content);

        // save sdk candidates
        SdkCandidates sdkCandidates = new SdkCandidates();
        sdkCandidates.setContent(content);
        this.sdkCandidatesRepository.save(sdkCandidates);
    }

    public Page<SdkCandidates> getSdkCandidatesList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        return this.sdkCandidatesRepository.findAll(pageable);
    }
}
