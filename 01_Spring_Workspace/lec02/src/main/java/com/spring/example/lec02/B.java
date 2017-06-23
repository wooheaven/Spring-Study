package com.spring.example.lec02;

public class B {
    private int firstNum;
    private int secondNum;

    public int getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(int firstNum) {
        this.firstNum = firstNum;
    }

    public int getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(int secondNum) {
        this.secondNum = secondNum;
    }

    public void add() {
        System.out.println(firstNum + secondNum);
    }
}
