package com.mysite.icloud.controller;

import java.awt.AWTException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.robot.MyRobot;

@RequestMapping("/icloud")
@Controller
public class IcloudController {

    @GetMapping("/moveFile")
    public String moveFile() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.mouseMove(500, 1350, 400);
        myRobot.mouseLeftClick(100);
        myRobot.mouseMove(500, 1790, 185);
        myRobot.mouseLeftClick(100);
        myRobot.altS(1000);
        return "redirect:/";
    }

    @GetMapping("/trash")
    public String trash() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.mouseMove(500, 1350, 400);
        myRobot.mouseLeftClick(100);
        myRobot.mouseMove(500, 1835, 185);
        myRobot.mouseLeftClick(100);
        myRobot.keyboardPressRelease(100, "");
        return "redirect:/";
    }

    @GetMapping("/etc")
    public String etc() throws AWTException {
        MyRobot myRobot = new MyRobot();
        /* moveFile */
        myRobot.mouseMove(500, 1350, 400);
        myRobot.mouseLeftClick(100);
        myRobot.mouseMove(500, 1790, 185);
        myRobot.mouseLeftClick(100);
        myRobot.altS(1000);
        /* trash */
        myRobot.mouseMove(500, 1350, 400);
        myRobot.mouseLeftClick(100);
        myRobot.mouseMove(500, 1835, 185);
        myRobot.mouseLeftClick(100);
        myRobot.keyboardPressRelease(100, "");
        /* click left */
        myRobot.mouseMove(500, 500, 500);
        myRobot.mouseLeftClick(100);
        return "redirect:/";
    }
}
