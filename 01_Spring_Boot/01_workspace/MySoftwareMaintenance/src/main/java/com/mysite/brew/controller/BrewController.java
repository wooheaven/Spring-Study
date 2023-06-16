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

import com.mysite.brew.model.BrewLs;
import com.mysite.brew.model.BrewOutdated;
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

    @GetMapping("ls")
    public String ls() throws IOException, InterruptedException, ExecutionException {
        brewService.ls();
        return "redirect:/brew/lsList";
    }

    @GetMapping("lsList")
    public String lsList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BrewLs> paging = this.brewService.getBrewLsList(page);
        model.addAttribute("paging", paging);
        return "brew_ls_list";
    }

    @GetMapping("tail")
    public String tail() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100, "cd /home/linuxbrew");
//        myRobot.openTerminal(100);
//        myRobot.keyboardPressRelease(1000, "cd /home/linuxbrew");
        myRobot.keyboardPressRelease(1000, "./02_tail_outdated.sh");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }

    @GetMapping("deps")
    public String deps() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100, "cd /home/linuxbrew");
//        myRobot.openTerminal(100);
//        myRobot.keyboardPressRelease(1000, "cd /home/linuxbrew");
        myRobot.keyboardPressRelease(1000, "./03_brew_deps.sh");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }

    @GetMapping("check")
    public String check() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100, "cd /home/linuxbrew");
//        myRobot.keyboardPressRelease(1000, "cd /home/linuxbrew");
        myRobot.keyboardPressRelease(1000, "./04_tail_check.sh");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }

    @GetMapping("etc")
    public String etc() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100, "cd /home/linuxbrew");
//        myRobot.openTerminal(100);
//        myRobot.keyboardPressRelease(1000, "cd /home/linuxbrew");
        myRobot.keyboardPressRelease(1000, "./08_05_06_07.sh");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }

    @GetMapping("cleanup")
    public String cleanup() throws IOException, InterruptedException, ExecutionException {
        brewService.cleanup();
        return "redirect:/";
    }

    @GetMapping("/upgrade/{name}")
    public String upgrade(@PathVariable("name") String name) throws IOException, InterruptedException, ExecutionException {
        System.out.println(String.format("brew upgrade %s", name));
        brewService.upgrade(name);
        return "redirect:/";
    }
}