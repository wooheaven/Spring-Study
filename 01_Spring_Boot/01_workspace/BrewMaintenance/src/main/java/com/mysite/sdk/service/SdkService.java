package com.mysite.sdk.service;

import com.google.gson.JsonArray;
import com.mysite.common.service.CommonService;
import com.mysite.sdk.entity.*;
import com.mysite.sdk.repository.*;
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
    private static final String sdkInit = "source $HOME/.sdkman/bin/sdkman-init.sh && ";
    private static final String sdkUpdate = sdkInit + "sdk selfupdate";
    private static final String sdkVersion = sdkInit + "sdk version";
    private static final String sdkList = sdkInit + "PAGER=cat sdk list %s | grep '|' | sed 's/ //g' | sed 's/|/,/g'";
    private static final String sdkCandidates = "tree -J -d -L 2 ~/.sdkman/candidates/ | jq -M";
    private static final String sdkInstall = sdkInit + "sdk install %s %s | sed -e 's/[\\x1B|\\[]//g;s/^1;32m//;s/0m$//;s/^1;33m//' ";
    private static final String sdkUninstall = sdkInit + "sdk uninstall %s %s | sed -e 's/[\\x1B|\\[]//g;s/^1;32m//;s/0m$//;s/^1;33m//' ";
    private static final String sdkUse = sdkInit + "sdk default %s %s | sed -e 's/[\\x1B|\\[]//g;s/^1;32m//;s/0m$//;s/^1;33m//' ";
    private final CommonService commonService;
    private final SdkUpdateRepository sdkUpdateRepository;
    private final SdkVersionRepository sdkVersionRepository;
    private final SdkListRepository sdkListRepository;
    private final SdkCandidatesRepository sdkCandidatesRepository;
    private final SdkInstallRepository sdkInstallRepository;
    private final SdkUninstallRepository sdkUninstallRepository;
    private final SdkUseRepository sdkUseRepository;

    @Autowired
    public SdkService(CommonService commonService,
                      SdkUpdateRepository sdkUpdateRepository,
                      SdkVersionRepository sdkVersionRepository,
                      SdkListRepository sdkListRepository,
                      SdkCandidatesRepository sdkCandidatesRepository,
                      SdkInstallRepository sdkInstallRepository,
                      SdkUninstallRepository sdkUninstallRepository,
                      SdkUseRepository sdkUseRepository) {
        this.commonService = commonService;
        this.sdkUpdateRepository = sdkUpdateRepository;
        this.sdkVersionRepository = sdkVersionRepository;
        this.sdkListRepository = sdkListRepository;
        this.sdkCandidatesRepository = sdkCandidatesRepository;
        this.sdkInstallRepository = sdkInstallRepository;
        this.sdkUninstallRepository = sdkUninstallRepository;
        this.sdkUseRepository = sdkUseRepository;
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

    public void install(String name, String identifier) throws Exception {
        // run sdk candidates
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            String cmd = String.format(sdkInstall, name, identifier);
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

        // save sdk version from content
        SdkInstall sdkInstall = new SdkInstall();
        sdkInstall.setContent(content);
        this.sdkInstallRepository.save(sdkInstall);
    }

    public Page<SdkInstall> getSdkInstallList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        return this.sdkInstallRepository.findAll(pageable);
    }

    public void uninstall(String name, String identifier) throws Exception {
        // run sdk candidates
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            String cmd = String.format(sdkUninstall, name, identifier);
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

        // save sdk version from content
        SdkUninstall sdkUninstall = new SdkUninstall();
        sdkUninstall.setContent(content);
        this.sdkUninstallRepository.save(sdkUninstall);
    }

    public Page<SdkUninstall> getSdkUninstallList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        return this.sdkUninstallRepository.findAll(pageable);
    }

    public void use(String name, String identifier) throws Exception {
        // run sdk use <candidate> <version>
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            String cmd = String.format(sdkUse, name, identifier);
            lineList = this.commonService.getLineListByTerminalOut(cmd);
        }

        // read sdk use <candidate> <version>
        String content = "";
        for (String myLine : lineList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replaceAll("\n$", "");

        // save sdk use <candidate> <version>
        SdkUse sdkUse = new SdkUse();
        sdkUse.setContent(content);
        this.sdkUseRepository.save(sdkUse);
    }

    public Page<SdkUse> getSdkUseList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 1000, Sort.by(sorts));
        return this.sdkUseRepository.findAll(pageable);
    }
}
