package com.apress.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apress.spring.domain.Journal;
import com.apress.spring.repository.JournalRepository;

@SpringBootApplication
public class SpringBootJournalApplication {

	@Bean
	InitializingBean saveData(JournalRepository repo) {
		return () -> {
			repo.save(new Journal("스피링 부트 입문", "오늘부터 스프링부트 공부하기 시작했다", "01/01/2016"));
			repo.save(new Journal("간단한 스피링부트 프로젝트", "처음 스프링부트 프로젝트를 만듬", "01/02/2016"));
		};
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootJournalApplication.class, args);
	}
}
