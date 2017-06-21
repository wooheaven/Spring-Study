package com.spring.example;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainClass {
	public static void main(String[] args) {
		// Only Java
		MyCalculator myCalculator = new MyCalculator();
		myCalculator.setCalculator(new Calculator());
		
		myCalculator.setFirstNum(10);
		myCalculator.setSecondNum(2);
		
		myCalculator.add();
		myCalculator.sub();
		myCalculator.mul();
		myCalculator.div();

		// Java + Spring Dependency Injection
		String configLocation = "classpath:applicationCTX.xml";
		AbstractApplicationContext ctx = new GenericXmlApplicationContext(configLocation);
		
		MyCalculator myCalculator2 = ctx.getBean("myCalculator", MyCalculator.class);
		
		myCalculator2.add();
		myCalculator2.sub();
		myCalculator2.mul();
		myCalculator2.div();
	}
}
