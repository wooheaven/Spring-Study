package com.mysite.snap.controller;

import com.mysite.snap.entity.SnapList;
import com.mysite.snap.entity.SnapRefreshList;
import com.mysite.snap.service.SnapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/snap")
@Controller
public class SnapController {
    private final SnapService snapService;

    @Autowired
    public SnapController(SnapService snapService) {
        this.snapService = snapService;
    }

    @GetMapping("/list")
    public String list() throws Exception {
        this.snapService.list();
        return "redirect:/snap/list/log";
    }
    @GetMapping("/list/log")
    public String listLog(Model model, @RequestParam(value = "page", defaultValue = "0") int page) throws Exception {
        Page<SnapList> paging = this.snapService.getSnapList(page);
        model.addAttribute("paging", paging);
        return "/snap/snap_list";
    }
    @GetMapping("/refreshList")
    public String refreshList() throws Exception {
        this.snapService.refreshList();
        return "redirect:/snap/refreshList/list";
    }

    @GetMapping("/refreshList/list")
    public String refreshListList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) throws Exception {
        Page<SnapRefreshList> paging = this.snapService.getSnapRefreshList(page);
        model.addAttribute("paging", paging);
        return "/snap/snap_refresh_list";
    }

    @GetMapping("/refresh/{name}")
    public String refreshName(@PathVariable("name") String name) throws Exception {
        this.snapService.refresh(name);
        return "redirect:/snap/refreshList";
    }
}
