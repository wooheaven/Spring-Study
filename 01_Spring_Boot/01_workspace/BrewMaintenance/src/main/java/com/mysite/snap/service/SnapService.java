package com.mysite.snap.service;

import com.mysite.common.service.CommonService;
import com.mysite.snap.entity.SnapChanges;
import com.mysite.snap.entity.SnapList;
import com.mysite.snap.entity.SnapRefreshList;
import com.mysite.snap.entity.SnapRemove;
import com.mysite.snap.repository.SnapChangesRepository;
import com.mysite.snap.repository.SnapListRepository;
import com.mysite.snap.repository.SnapRefreshListRepository;
import com.mysite.snap.repository.SnapRemoveRepository;
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
    private static final String snapList = "sudo -S <<< \"12qwas\" snap list --all 2>&1 | sed -e 's/\\[sudo\\] password for woo: //' | xargs -rn6";
    private static final String snapRemove = "sudo -S <<< \"12qwas\" snap remove ";
    private static final String snapRefreshList = "sudo -S <<< \"12qwas\" snap refresh --list 2>&1 | xargs -r ";
    private static final String snapRefresh = "sudo -S <<< \"12qwas\" snap refresh ";
    private static final String snapChanges = "sudo -S <<< \"12qwas\" snap changes | sed -e 's/\s\\{3,\\}/  /g' ";
    private final CommonService commonService;
    private final SnapRefreshListRepository snapRefreshListRepository;
    private final SnapListRepository snapListRepository;
    private final SnapRemoveRepository snapRemoveRepository;
    private final SnapChangesRepository snapChangesRepository;

    @Autowired
    public SnapService(CommonService commonService,
                       SnapRefreshListRepository snapRefreshListRepository,
                       SnapListRepository snaplistRepository,
                       SnapRemoveRepository snapRemoveRepository,
                       SnapChangesRepository snapChangesRepository) {
        this.commonService = commonService;
        this.snapRefreshListRepository = snapRefreshListRepository;
        this.snapListRepository = snaplistRepository;
        this.snapRemoveRepository = snapRemoveRepository;
        this.snapChangesRepository = snapChangesRepository;
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
        content = content.replace("Name Version Rev Size Publisher Notes ", "");
        content = content.replaceAll("\n$", "");

        // save snap refresh list
        SnapRefreshList snapRefreshList = null;
        if (content.contains("All snaps up to date.")) {
            snapRefreshList = new SnapRefreshList();
            snapRefreshList.setRefreshLog(content);
            this.snapRefreshListRepository.save(snapRefreshList);
        } else {
            String[] fields = content.split(" ");
            int snapRefreshLineCount = fields.length / 6;
            for (int i = 0; i < snapRefreshLineCount; i++) {
                snapRefreshList = new SnapRefreshList();
                snapRefreshList.setName(fields[i*6 + 0]);
                snapRefreshList.setVersion(fields[i*6 + 1]);
                snapRefreshList.setRev(fields[i*6 + 2]);
                snapRefreshList.setSize(fields[i*6 + 3]);
                snapRefreshList.setPublisher(fields[i*6 + 4]);
                snapRefreshList.setNotes(fields[i*6 + 5]);
                this.snapRefreshListRepository.save(snapRefreshList);
            }
        }
    }

    public Page<SnapRefreshList> getSnapRefreshList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.snapRefreshListRepository.findAll(pageable);
    }

    public void refresh(String name) throws Exception {
        // find last snap refresh name from table
        SnapRefreshList snapRefreshList = this.snapRefreshListRepository.findOneCustom(name);

        // run snap refresh name
        List<String> lineList = this.commonService.getLineListByTerminalOut(snapRefresh + name + " 2>&1");

        // read snap refresh name
        String content = "";
        for (String myLine : lineList) {
            if (myLine.length() > 0) {
                content += myLine;
                content += "\n";
            }
        }
        content = content.replace("[sudo] password for woo: ", "");
        content = content.replaceAll("\n$", "");

        // write snap refresh name to table
        snapRefreshList.setRefreshLog(content);
        this.snapRefreshListRepository.save(snapRefreshList);
    }

    public Page<SnapList> getSnapList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(page, 100, Sort.by(sorts));
        return this.snapListRepository.findAll(pageable);
    }

    public void list() throws Exception {
        // clear before snal list -all
        this.snapListRepository.deleteAll();

        // run snap list --all
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = this.commonService.getLineListByTerminalOut(snapList);
        }

        // read snap list --all
        String content = "";
        for (String myLine : lineList) {
            content += myLine;
            content += "\n";
        }
        content = content.replace("Name Version Rev Tracking Publisher Notes\n", "");
        content = content.replaceAll("\n$", "");

        // save snap list --all to table
        for (String line : content.split("\n")) {
            String[] fields = line.split(" ");
            SnapList snapList = new SnapList();
            if (fields.length == 6) {
                snapList.setName(fields[0]);
                snapList.setVersion(fields[1]);
                snapList.setRev(fields[2]);
                snapList.setTracking(fields[3]);
                snapList.setPublisher(fields[4]);
                snapList.setNotes(fields[5]);
            }
            this.snapListRepository.save(snapList);
        }
    }

    public void remove(String name, String rev) throws Exception {
        // run snap remove
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = this.commonService.getLineListByTerminalOut(snapRemove + name + " --revision " + rev);
        }

        // read snap remove
        String content = "";
        for (String myLine : lineList) {
            content += myLine;
            content += "\n";
        }
        content = content.replaceAll("\n$", "");

        // save snap remove
        SnapRemove snapRemove = new SnapRemove();
        snapRemove.setName(name);
        snapRemove.setRemoveLog(content);
        this.snapRemoveRepository.save(snapRemove);
    }

    public Page<SnapRemove> getSnapRemoveLog(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 100, Sort.by(sorts));
        return this.snapRemoveRepository.findAll(pageable);
    }

    public void changes() throws Exception {
        // run snap changes
        List<String> lineList = new ArrayList<>();
        while (0 == lineList.size()) {
            lineList = this.commonService.getLineListByTerminalOut(snapChanges);
        }

        // read snap changes
        String content = "";
        for (String myLine : lineList) {
            content += myLine;
            content += "\n";
        }
        content = content.replaceAll("\n$", "");

        // save snap changes
        SnapChanges snapChanges = new SnapChanges();
        if (content.contains("ID  Status  Spawn  Ready  Summary")) {
            content = content.replace("ID  Status  Spawn  Ready  Summary\n", "");
            String[] fields = content.split("  ");
            if (fields.length == 5) {
                snapChanges.setId(Long.valueOf(fields[0]));
                snapChanges.setStatus(fields[1]);
                snapChanges.setSpawn(fields[2]);
                snapChanges.setReady(fields[3]);
                snapChanges.setSummary(fields[4]);
            }
        }
        if (snapChanges.getId() != null) {
            this.snapChangesRepository.save(snapChanges);
        }
    }

    public Page<SnapChanges> getSnapChanges(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 100, Sort.by(sorts));
        return this.snapChangesRepository.findAll(pageable);
    }
}
