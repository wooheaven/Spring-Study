package com.spring.example.lec07.ex1;

import org.springframework.context.support.GenericXmlApplicationContext;

public class MainClass {
	public static void main(String[] args) {
		// Spring Container Generate
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		
		// Spirng Container Configure
		ctx.load("classpath:applicationCTX1.xml");
		ctx.refresh();
		
		// Spring Container Use
		Student student = ctx.getBean("student", Student.class);
		System.out.println("이름 : " + student.getName());
		System.out.println("나이 : " + student.getAge());
		
		// Spring Container End
		ctx.close();
	}
}
