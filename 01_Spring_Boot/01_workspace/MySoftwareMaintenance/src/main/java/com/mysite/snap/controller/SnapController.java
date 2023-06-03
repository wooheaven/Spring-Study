package com.mysite.snap.controller;

import java.awt.AWTException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.robot.MyRobot;

@RequestMapping("/snap")
@Controller
public class SnapController {
    @GetMapping("/changes")
    public String changes() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100);
        myRobot.keyboardPressRelease(1000, "sudo snap changes");
        myRobot.keyboardPressRelease(1000, "12qwas");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String list() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100);
        myRobot.keyboardPressRelease(1000, "sudo snap list --all");
        myRobot.keyboardPressRelease(1000, "12qwas");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }

    @GetMapping("/refresh/list")
    public String refreshList() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100);
        myRobot.keyboardPressRelease(1000, "sudo snap refresh --list");
        myRobot.keyboardPressRelease(1000, "12qwas");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }
    
    @GetMapping("/refresh/target")
    public String refreshTarget()throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100);
        myRobot.keyboardPressRelease(1000, 
                "sudo snap refresh --list 2>&1 | awk 'BEGIN{FS=OFS=\" \"} {if (NF == 6 && $NF != \"Notes\") {print $1} }' | xargs -rn1 sudo snap refresh ");
        myRobot.keyboardPressRelease(1000, "12qwas");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }

    @GetMapping("/remove")
    public String remove() throws AWTException {
        MyRobot myRobot = new MyRobot();
        myRobot.openTerminal(100);
        myRobot.keyboardPressRelease(1000, 
                "sudo snap list --all | awk '/disabled/{print $1\" --revision \"$3}' | xargs -rn3 sudo snap remove");
        myRobot.keyboardPressRelease(1000, "12qwas");
        myRobot.goToFireFox(1000);
        return "redirect:/";
    }
}
