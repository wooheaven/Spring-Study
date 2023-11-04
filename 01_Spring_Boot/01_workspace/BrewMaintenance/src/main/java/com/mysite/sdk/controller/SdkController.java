package com.mysite.sdk.controller;

import com.mysite.sdk.entity.*;
import com.mysite.sdk.service.SdkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("update")
    public String update() throws Exception {
        this.sdkService.update();
        return "redirect:/sdk/updateList";
    }

    @GetMapping("updateList")
    public String updateList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<SdkUpdate> paging = this.sdkService.getSdkUpdateList(page);
        model.addAttribute("paging", paging);
        return "sdk/sdk_update_list";
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
    @GetMapping("list/{name}")
    public String list(@PathVariable("name") String name,
                       Model model, @RequestParam(value = "page", defaultValue = "0") int page) throws Exception {
        this.sdkService.list(name);
        Page<SdkList> paging = this.sdkService.getSdkList(page);
        model.addAttribute("paging", paging);
        return "sdk/sdk_list";
    }

    @GetMapping("candidates")
    public String candidates(Model model, @RequestParam(value = "page", defaultValue = "0") int page) throws Exception {
        this.sdkService.getSdkCandidates();
        Page<SdkCandidates> paging = this.sdkService.getSdkCandidatesList(page);
        model.addAttribute("paging", paging);
        return "sdk/sdk_candidates_list";
    }

    @GetMapping("install/{name}/{identifier}")
    public String install(@PathVariable("name") String name,
                          @PathVariable("identifier") String identifier) throws Exception {
        this.sdkService.install(name, identifier);
        return "redirect:/sdk/install/list";
    }

    @GetMapping("install/list")
    public String installList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<SdkInstall> paging = this.sdkService.getSdkInstallList(page);
        model.addAttribute("paging", paging);
        return "sdk/sdk_install_list";
    }

    @GetMapping("uninstall/{name}/{identifier}")
    public String uninstall(@PathVariable("name") String name,
                            @PathVariable("identifier") String identifier) throws Exception {
        this.sdkService.uninstall(name, identifier);
        return "redirect:/sdk/uninstall/list";
    }

    @GetMapping("uninstall/list")
    public String uninstallList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<SdkUninstall> paging = this.sdkService.getSdkUninstallList(page);
        model.addAttribute("paging", paging);
        return "sdk/sdk_uninstall_list";
    }
}
