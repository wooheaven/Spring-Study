package com.mysite.robot;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.springframework.stereotype.Component;

@Component
public class MyRobot {
    private Robot robot;

    public MyRobot() throws AWTException {
        System.setProperty("java.awt.headless", "false");
        this.robot = new Robot();
    }

    public void mouseMove(int delay, int x, int y) {
        delay(delay);
        this.robot.mouseMove(x, y);
    }

    public void mouseRightClick(int delay) {
        delay(delay);
        this.robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        this.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public void mouseLeftClick(int delay) {
        delay(delay);
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void delay(int ms) {
        this.robot.delay(ms);
    }

    public void altS(int ms) {
        delay(ms);
        this.robot.keyPress(KeyEvent.VK_ALT);
        this.robot.keyPress(KeyEvent.VK_S);
        this.robot.keyRelease(KeyEvent.VK_S);
        this.robot.keyRelease(KeyEvent.VK_ALT);
        delay(ms);
    }

    public void mouseWheel(int delay) {
        delay(delay);
        for(int i = 0; i<20; i++) {
            this.robot.mouseWheel(1);
            delay(100);
        }
        for(int i = 0; i<21; i++) {
            this.robot.mouseWheel(-1);
            delay(100);
        }
    }
}
