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
        int delay = 850;
        myRobot.mouseMove(delay, 420, 350);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseRightClick(delay);
        myRobot.mouseMove(delay, 450, 410);
        myRobot.mouseLeftClick(delay * 4);
        myRobot.altS(delay * 4);

        myRobot.mouseMove(delay, 420, 350);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseRightClick(delay);
        myRobot.mouseMove(delay, 450, 440);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseMove(delay, 1160, 650);
        myRobot.mouseLeftClick(delay * 5);
        myRobot.altS(delay * 6);
    }

    private void goToBase(MyRobot myRobot) {
        int delay = 600;
        myRobot.mouseMove(delay, 500, 1450);
        myRobot.mouseLeftClick(delay);
    }

    @GetMapping("/trash")
    public String trash() throws AWTException {
        MyRobot myRobot = new MyRobot();
        trash(myRobot);
        goToBase(myRobot);
        return "redirect:/";
    }

    private void trash(MyRobot myRobot) throws AWTException {
        int delay = 700;
        myRobot.mouseMove(delay, 420, 350);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseRightClick(delay);
        myRobot.mouseMove(delay, 450, 610);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseMove(delay, 1090, 740);
        myRobot.mouseLeftClick(delay);
        myRobot.delay(delay * 3);
    }

    @GetMapping("/etc")
    public String etc() throws AWTException {
        MyRobot myRobot = new MyRobot();
        moveFile(myRobot);
        trash(myRobot);
        return "redirect:/";
    }
}
