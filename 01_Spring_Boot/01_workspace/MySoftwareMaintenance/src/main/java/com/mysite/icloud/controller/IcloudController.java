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
        moveFile(myRobot);
        goToBase(myRobot);
        return "redirect:/";
    }

    private void moveFile(MyRobot myRobot) throws AWTException {
        myRobot.mouseMove(500, 1350, 400);
        myRobot.mouseLeftClick(100);
        myRobot.mouseMove(500, 1790, 185);
        myRobot.mouseLeftClick(2500);
        myRobot.altS(1100);
    }

    private void goToBase(MyRobot myRobot) throws AWTException {
        myRobot.mouseMove(500, 790, 185);
        myRobot.mouseLeftClick(100);
    }

    @GetMapping("/trash")
    public String trash() throws AWTException {
        MyRobot myRobot = new MyRobot();
        trash(myRobot);
        goToBase(myRobot);
        return "redirect:/";
    }

    private void trash(MyRobot myRobot) throws AWTException {
        myRobot.mouseMove(500, 1350, 400);
        myRobot.mouseLeftClick(100);
        myRobot.mouseMove(500, 1835, 185);
        myRobot.mouseLeftClick(100);
        myRobot.keyboardPressRelease(100, "");
    }

    @GetMapping("/etc")
    public String etc() throws AWTException {
        MyRobot myRobot = new MyRobot();
        moveFile(myRobot);
        trash(myRobot);
        goToBase(myRobot);
        return "redirect:/";
    }
}
