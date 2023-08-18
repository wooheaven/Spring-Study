package com.mysite.snap.service;

import com.mysite.common.service.CommonService;
import com.mysite.snap.entity.SnapRefreshList;
import com.mysite.snap.repository.SnapRefreshListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SnapService {
    private static final String snapRefreshList = "sudo -S <<< \"12qwas\" snap refresh --list 2>&1 | xargs -r";
    private final CommonService commonService;
    private final SnapRefreshListRepository snapRefreshListRepository;

    @Autowired
    public SnapService(CommonService commonService, SnapRefreshListRepository snapRefreshListRepository) {
        this.commonService = commonService;
        this.snapRefreshListRepository = snapRefreshListRepository;
    }

    public void refreshList() throws Exception {
        // run snap refresh list
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = this.commonService.getLineListByTerminalOut(snapRefreshList);
        }

        // read snap refresh list
        String content = "";
        for (String myLine : lineList) {
            content += myLine;
            content += "\n";
        }
        content = content.replace("[sudo] password for woo: ", "");
        content = content.replaceAll("\n$", "");

        // save snap refresh list
        SnapRefreshList snapRefreshList = new SnapRefreshList();
        snapRefreshList.setContent(content);
        this.snapRefreshListRepository.save(snapRefreshList);
    }

    public Page<SnapRefreshList> getSnapRefreshList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.snapRefreshListRepository.findAll(pageable);
    }
}
