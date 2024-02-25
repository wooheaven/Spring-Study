package com.mysite.icloud.controller;

import java.awt.AWTException;

import com.mysite.robot.MyRobot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/icloud")
@Controller
public class IcloudController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/moveFile")
    public String moveFile() throws AWTException {
        MyRobot myRobot = new MyRobot();
        moveFile(myRobot);
        goToBase(myRobot);
        return "redirect:/";
    }

    private void moveFile(MyRobot myRobot) throws AWTException {
        int delay = 850;
        int firstPictureX = 430;
        int firstPictureY = 380;
        int secondDownloadX = firstPictureX + 30;
        int secondDownloadY = firstPictureY + 65;
        int thirdDownloadY = firstPictureY + 95;
        // Download
        myRobot.mouseMove(delay, firstPictureX, firstPictureY);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseRightClick(delay);
        myRobot.mouseMove(delay, secondDownloadX, secondDownloadY);
        myRobot.mouseLeftClick(delay * 4);
        myRobot.altS(delay * 4);

        // More Download Options
        myRobot.mouseMove(delay, firstPictureX, firstPictureY);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseRightClick(delay);
        myRobot.mouseMove(delay, secondDownloadX, thirdDownloadY);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseMove(delay, 1160, 670);
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
        int delay = 850;
        int firstPictureX = 430;
        int firstPictureY = 380;
        int secondPictureX = firstPictureX + 30;
        int secondPictureY = firstPictureY + 265;
        myRobot.mouseMove(delay, firstPictureX, firstPictureY);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseRightClick(delay);
        myRobot.mouseMove(delay, secondPictureX, secondPictureY);
        myRobot.mouseLeftClick(delay);
        myRobot.mouseMove(delay, 1090, 740);
        myRobot.mouseLeftClick(delay);
        myRobot.delay(delay * 3);
    }

    @GetMapping("/etc")
    public String etc(@RequestParam(value = "count", defaultValue = "1") Integer count) throws AWTException {
        logger.info("count " + count + " is requested");
        for (int i = 1; i <= count; i++) {
            logger.info("current count is " + i);
            MyRobot myRobot = new MyRobot();
            moveFile(myRobot);
            trash(myRobot);
            goToBase(myRobot);
        }
        return "redirect:/";
    }
}
