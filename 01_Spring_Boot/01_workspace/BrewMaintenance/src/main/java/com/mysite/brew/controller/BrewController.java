package com.mysite.brew.controller;

import com.mysite.brew.model.BrewUpdate;
import com.mysite.brew.service.BrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/brew")
@Controller
public class BrewController {
    private final BrewService brewService;
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
}
