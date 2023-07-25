package com.mysite.sdk.controller;

import java.awt.AWTException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.robot.MyRobot;

@RequestMapping("/sdk")
@Controller
public class SdkController {
    @GetMapping("/list/java")
    public String listJava() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100);
        myRobot.keyboardPressRelease(1000, "sdk list java");
        return "redirect:/";
    }
    
    @GetMapping("/candidates/java")
    public String candidateJava()throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100);
        myRobot.keyboardPressRelease(1000, "tree -d -L 2 /home/woo/.sdkman/candidates/");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }
}