package com.mysite.brew.controller;

import com.mysite.brew.entity.*;
import com.mysite.brew.service.BrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/brew")
@Controller
public class BrewController {
    private final BrewService brewService;

    @Autowired
    public BrewController(BrewService brewService) {
        this.brewService = brewService;
    }

    @GetMapping("update")
    public String update() throws Exception {
        brewService.update();
        return "redirect:/brew/updateList";
    }

    @GetMapping("updateList")
    public String updateList(Model model, @RequestParam(value = "page" , defaultValue = "0") int page) {
        Page<BrewUpdate> paging = this.brewService.getBrewUpdateList(page);
        model.addAttribute("paging" , paging);
        return "/brew/brew_update_list";
    }

    @GetMapping("outdated")
    public String outdated() throws Exception {
        brewService.outdated();
        return "redirect:/brew/outdatedList";
    }

    @GetMapping("outdatedList")
    public String outdatedList(Model model, @RequestParam(value = "page" , defaultValue = "0") int page) {
        Page<BrewOutdated> paging = this.brewService.getBrewOutdatedList(page);
        model.addAttribute("paging" , paging);
        return "/brew/brew_outdated_list";
    }

    @GetMapping("outdatedPivot")
    public String outdatedPivot() throws Exception {
        brewService.outdatedPivot();
        return "redirect:/brew/outdatedPivotList";
    }

    @GetMapping("outdatedPivotList")
    public String outdatedPivotList(Model model, @RequestParam(value = "page" , defaultValue = "0") int page) {
        Page<BrewOutdatedPivot> paging = this.brewService.getBrewOutdatedPivotList(page);
        model.addAttribute("paging" , paging);
        return "/brew/brew_outdated_pivot_list";
    }

    @GetMapping("deps")
    public String deps() throws Exception {
        brewService.deps();
        return "redirect:/brew/depsList";
    }

    @GetMapping("depsList")
    public String depsList(Model model, @RequestParam(value = "page" , defaultValue = "0") int page) {
        Page<BrewDeps> paging = this.brewService.getBrewDepsList(page);
        model.addAttribute("paging" , paging);
        return "/brew/brew_deps_list";
    }

    @GetMapping("/upgrade/{name}")
    public String upgrade(@PathVariable("name") String name) throws Exception {
        brewService.upgrade(name);
        return "redirect:/brew/delete/{name}";
    }

    @GetMapping("/delete/{name}")
    public String delete(@PathVariable("name") String name) {
        brewService.delete(name);
        return "redirect:/brew/depsList";
    }

    @GetMapping("autoremove")
    public String autoremove() throws Exception {
        brewService.autoremove();
        return "redirect:/brew/autoremove/list";
    }

    @GetMapping("autoremove/list")
    public String autoremoveList(Model model, @RequestParam(value = "page" , defaultValue = "0") int page) {
        Page<BrewAutoremove> paging = this.brewService.getBrewAutoremoveList(page);
        model.addAttribute("paging" , paging);
        return "/brew/brew_autoremove_list";
    }
    @GetMapping("cleanup")
    public String cleanup() throws Exception {
        brewService.cleanup();
        return "redirect:/brew/cleanup/list";
    }

    @GetMapping("cleanup/list")
    public String cleanupList(Model model, @RequestParam(value = "page" , defaultValue = "0") int page) {
        Page<BrewClean> paging = this.brewService.getBrewCleanupList(page);
        model.addAttribute("paging" , paging);
        return "/brew/brew_cleanup_list";
    }

    @GetMapping("doctor")
    public String doctor() throws Exception {
        brewService.doctor();
        return "redirect:/brew/doctor/list";
    }

    @GetMapping("doctor/list")
    public String doctorList(Model model, @RequestParam(value = "page" , defaultValue = "0") int page) {
        Page<BrewDoctor> paging = this.brewService.getBrewDoctorList(page);
        model.addAttribute("paging" , paging);
        return "/brew/brew_doctor_list";
    }

    @GetMapping("list")
    public String list() throws Exception {
        brewService.list();
        return "redirect:/brew/list/last";
    }

    @GetMapping("list/last")
    public String listLast(Model model, @RequestParam(value = "page" , defaultValue = "0") int page) {
        Page<BrewList> paging = this.brewService.getBrewListLast(page);
        model.addAttribute("paging" , paging);
        return "/brew/brew_list_last";
    }

    @GetMapping("list/all")
    public String listAll(Model model, @RequestParam(value = "page" , defaultValue = "0") int page) {
        Page<BrewList> paging = this.brewService.getBrewListAll(page);
        model.addAttribute("paging" , paging);
        return "/brew/brew_list_all";
    }

    @GetMapping("/deps/{name}")
    public String depsInstalled(@PathVariable("name") String name) throws Exception {
        brewService.depsInstalled(name);
        return "redirect:/brew/deps/installed/all";
    }

    @GetMapping("/deps/installed/all")
    public String depsInstalledAll(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BrewDepsInstalled> paging = this.brewService.getBrewDepsInstalled(page);
        model.addAttribute("paging", paging);
        return "/brew/brew_deps_installed";
    }

    @GetMapping("/uses/{name}")
    public String usesInstalled(@PathVariable("name") String name) throws Exception {
        brewService.usesInstalled(name);
        return "redirect:/brew/uses/installed/all";
    }

    @GetMapping("/uses/installed/all")
    public String usesInstalledAll(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BrewUsesInstalled> paging = this.brewService.getBrewUsesInstalled(page);
        model.addAttribute("paging", paging);
        return "/brew/brew_uses_installed";
    }

    @GetMapping("/info/{name}")
    public String info(@PathVariable("name") String name) throws Exception {
        brewService.info(name);
        return "redirect:/brew/info/installed/all";
    }

    @GetMapping("/info/installed/all")
    public String infoInstalledAll(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BrewInfo> paging = this.brewService.getBrewInfo(page);
        model.addAttribute("paging", paging);
        return "/brew/brew_info_installed";
    }
}