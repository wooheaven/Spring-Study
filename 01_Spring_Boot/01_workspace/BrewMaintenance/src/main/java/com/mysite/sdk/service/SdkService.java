package com.mysite.sdk.service;

import com.mysite.common.service.CommonService;
import com.mysite.sdk.entity.SdkVersion;
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
    private static final String sdkVersion= "source \"$HOME/.sdkman/bin/sdkman-init.sh\" && sdk version";
    private final CommonService commonService;
    private final SdkVersionRepository sdkVersionRepository;

    @Autowired
    public SdkService(CommonService commonService, SdkVersionRepository sdkVersionRepository) {
        this.commonService = commonService;
        this.sdkVersionRepository = sdkVersionRepository;
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
