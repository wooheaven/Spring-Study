package com.spring.example.lec07.ex2;

import org.springframework.context.support.GenericXmlApplicationContext;

public class MainClass {
	public static void main(String[] args) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		
		ctx.load("classpath:applicationCTX2.xml");
		
		// Spring Bean Generate
		ctx.refresh();
		
		// Spring Bean destroy
		ctx.close();
	}
}
