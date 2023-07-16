package com.mysite.brew.controller;

import com.mysite.brew.entity.BrewDeps;
import com.mysite.brew.entity.BrewOutdated;
import com.mysite.brew.entity.BrewOutdatedPivot;
import com.mysite.brew.entity.BrewUpdate;
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
    public String deps() throws Exception {
        brewService.deps();
        return "redirect:/brew/depsList";
    }

    @GetMapping("depsList")
    public String depsList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<BrewDeps> paging = this.brewService.getBrewDepsList(page);
        model.addAttribute("paging", paging);
        return "brew_deps_list";
    }

    @GetMapping("/upgrade/{name}")
    public String upgrade(@PathVariable("name") String name) throws Exception {
        brewService.upgrade(name);
        return "redirect:/";
    }

    @GetMapping("cleanup")
    public String cleanup() throws Exception {
        brewService.cleanup();
        return "redirect:/";
    }
}
