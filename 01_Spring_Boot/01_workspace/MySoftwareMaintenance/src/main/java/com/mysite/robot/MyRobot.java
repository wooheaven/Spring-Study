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

    public void openTerminal(int delay) {
        openTerminal(delay, "cd ~");
    }

    public void openTerminal(int delay, String path) {
        mouseMove(delay, 40, 60);
        mouseLeftClick(100);
        mouseMove(100, 500, 300);
        mouseRightClick(100);
        delay(100);
        this.robot.keyPress(KeyEvent.VK_UP);
        this.robot.keyRelease(KeyEvent.VK_UP);
        delay(100);
        keyboardPressRelease(500, "t");
        delay(100);
        keyboardPressRelease(500, path);
    }

    public void mouseMove(int delay, int x, int y) {
        delay(delay);
        this.robot.mouseMove(x, y);
    }

    private void mouseRightClick(int delay) {
        delay(delay);
        this.robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        this.robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public void mouseLeftClick(int delay) {
        delay(delay);
        this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void keyboardPressRelease(int delay, String cmdStr) {
        delay(delay);

        System.out.println(cmdStr);
        char[] cmdCharArray = cmdStr.toCharArray();
        int keyCode = 0;
        for (int i = 0; i < cmdCharArray.length; i++) {
            if (cmdCharArray[i] == '$') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(36);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_4);
                this.robot.keyRelease(KeyEvent.VK_4);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (cmdCharArray[i] == '{') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(123);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_OPEN_BRACKET);
                this.robot.keyRelease(KeyEvent.VK_OPEN_BRACKET);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (cmdCharArray[i] == '|') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(124);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_BACK_SLASH);
                this.robot.keyRelease(KeyEvent.VK_BACK_SLASH);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (cmdCharArray[i] == '}') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(125);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_CLOSE_BRACKET);
                this.robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (cmdCharArray[i] == '&') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(150);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_AMPERSAND);
                this.robot.keyRelease(KeyEvent.VK_AMPERSAND);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (cmdCharArray[i] == '\"') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(152);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_QUOTEDBL);
                this.robot.keyRelease(KeyEvent.VK_QUOTEDBL);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (cmdCharArray[i] == '>') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(160);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_GREATER);
                this.robot.keyRelease(KeyEvent.VK_GREATER);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (cmdCharArray[i] == '~') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(192);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_BACK_QUOTE);
                this.robot.keyRelease(KeyEvent.VK_BACK_QUOTE);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (cmdCharArray[i] == '!') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(517);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_1);
                this.robot.keyRelease(KeyEvent.VK_1);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            }
            else if (cmdCharArray[i] == '_') {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(523);
                this.robot.keyPress(KeyEvent.VK_SHIFT);
                this.robot.keyPress(KeyEvent.VK_UNDERSCORE);
                this.robot.keyRelease(KeyEvent.VK_UNDERSCORE);
                this.robot.keyRelease(KeyEvent.VK_SHIFT);
            } else {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(cmdCharArray[i]);
                if (Character.isUpperCase(cmdCharArray[i])) {
                    this.robot.keyPress(KeyEvent.VK_SHIFT);
                    this.robot.keyPress(keyCode);
                    this.robot.keyRelease(keyCode);
                    this.robot.keyRelease(KeyEvent.VK_SHIFT);
                } else {
                    this.robot.keyPress(keyCode);
                    this.robot.keyRelease(keyCode);
                }
            }
            System.out.println("" + cmdCharArray[i] + "\t- " + keyCode + "\t- " + KeyEvent.getKeyText(keyCode));
        }

        delay(700);
        this.robot.keyPress(KeyEvent.VK_ENTER);
        this.robot.keyRelease(KeyEvent.VK_ENTER);
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

    public void goToFireFox(int ms) {
        delay(ms);
        mouseMove(ms, 1800, 200);
        mouseLeftClick(100);
        delay(ms);
    }
}
