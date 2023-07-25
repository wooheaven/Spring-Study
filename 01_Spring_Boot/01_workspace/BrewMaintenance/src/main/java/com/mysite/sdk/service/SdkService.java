package com.mysite.sdk.service;

import com.mysite.common.service.CommonService;
import com.mysite.sdk.entity.SdkUpdate;
import com.mysite.sdk.entity.SdkVersion;
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
    private static final String sdkInit = "source \"$HOME/.sdkman/bin/sdkman-init.sh\" && ";
    private static final String sdkUpdate = sdkInit + "sdk update";
    private static final String sdkVersion = sdkInit + "sdk version";
    private final CommonService commonService;
    private final SdkUpdateRepository sdkUpdateRepository;
    private final SdkVersionRepository sdkVersionRepository;

    @Autowired
    public SdkService(CommonService commonService,
                      SdkUpdateRepository sdkUpdateRepository,
                      SdkVersionRepository sdkVersionRepository) {
        this.commonService = commonService;
        this.sdkUpdateRepository = sdkUpdateRepository;
        this.sdkVersionRepository = sdkVersionRepository;
    }

    public void update() throws Exception {
        // run sdk update
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = this.commonService.runByProcessBuilder(sdkUpdate);
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
            lineList = this.commonService.runByProcessBuilder(sdkVersion);
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
}
