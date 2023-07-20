package com.mysite.sdk.controller;

import com.mysite.sdk.entity.SdkVersion;
import com.mysite.sdk.service.SdkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/sdk")
@Controller
public class SdkController {
    private final SdkService sdkService;

    @Autowired
    public SdkController(SdkService sdkService) {
        this.sdkService = sdkService;
    }

    @GetMapping("version")
    public String version() throws Exception {
        this.sdkService.version();
        return "redirect:/sdk/versionList";
    }

    @GetMapping("versionList")
    public String versionList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<SdkVersion> paging = this.sdkService.getSdkVersionList(page);
        model.addAttribute("paging", paging);
        return "sdk/sdk_version_list";
    }
}
