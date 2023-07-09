package com.mysite.brew.controller;

import java.awt.AWTException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.brew.model.BrewDeps;
import com.mysite.brew.model.BrewLs;
import com.mysite.brew.model.BrewOutdated;
import com.mysite.brew.model.BrewOutdatedPivot;
import com.mysite.brew.model.BrewUpdate;
import com.mysite.brew.service.BrewService;
import com.mysite.robot.MyRobot;

import lombok.RequiredArgsConstructor;

@RequestMapping("/brew")
@RequiredArgsConstructor
@Controller
public class BrewController {
    private final BrewService brewService;

    @GetMapping("update")
    public String update() throws AWTException, IOException, InterruptedException, ExecutionException {
        brewService.update();
        return "redirect:/brew/updateList";
    }

    @GetMapping("updateList")
    public String updateList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BrewUpdate> paging = this.brewService.getBrewUpdateList(page);
        model.addAttribute("paging", paging);
        return "brew_update_list";
    }

    @GetMapping("outdated")
    public String outdated() throws Exception {
        brewService.outdated();
        return "redirect:/brew/outdatedList";
    }

    @GetMapping("outdatedList")
    public String outdatedList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BrewOutdated> paging = this.brewService.getBrewOutdatedList(page);
        model.addAttribute("paging", paging);
        return "brew_outdated_list";
    }

    @GetMapping("outdatedPivot")
    public String outdatedPivot() throws Exception {
        brewService.outdatedPivot();
        return "redirect:/brew/outdatedPivotList";
    }

    @GetMapping("outdatedPivotList")
    public String outdatedPivotList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BrewOutdatedPivot> paging = this.brewService.getBrewOutdatedPivotList(page);
        model.addAttribute("paging", paging);
        return "brew_outdated_pivot_list";
    }

    @GetMapping("deps")
    public String deps() throws IOException, InterruptedException, ExecutionException {
        brewService.deps();
        return "redirect:/brew/depsList";
    }

    @GetMapping("depsList")
    public String depsList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BrewDeps> paging = this.brewService.getBrewDepsList(page);
        model.addAttribute("paging", paging);
        return "brew_deps_list";
    }

    @GetMapping("cleanup")
    public String cleanup() throws IOException, InterruptedException, ExecutionException {
        brewService.cleanup();
        return "redirect:/";
    }

    @GetMapping("/upgrade/{name}")
    public String upgrade(@PathVariable("name") String name)
            throws IOException, InterruptedException, ExecutionException {
        System.out.println(String.format("brew upgrade %s", name));
        brewService.upgrade(name);
        return "redirect:/";
    }
}