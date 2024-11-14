package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;

@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void testJpaFindAll() {
        this.questionRepository.deleteAll();

        Question q1 = new Question();
        q1.setSubject("what is sbb?");
        q1.setContent("I want to know sbb.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);

        List<Question> all = this.questionRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testJpaFindById() {
        Integer lastId = null;
        Optional<Question> oq1=  this.questionRepository.findFirstByOrderByIdDesc();
        if (oq1.isPresent()) {
            Question q1 = oq1.get();
            lastId = q1.getId();
        }
        Optional<Question> oq = this.questionRepository.findById(lastId);
        if (oq.isPresent()) {
            Question q = oq.get();
            assertEquals("스프링부트 모델 질문입니다.", q.getSubject());
        }
    }

    @Test
    void testJpaFindBySubject() {
        Question q = this.questionRepository.findBySubject("what is sbb?");
        assertEquals("I want to know sbb.", q.getContent());
    }
    
    @Test
    void testJpaFindBySubjectAndContent() {
        Integer lastId = null;
        Optional<Question> oq1=  this.questionRepository.findFirstByOrderByIdDesc();
        if (oq1.isPresent()) {
            Question q1 = oq1.get();
            lastId = q1.getId();
        }
        Optional<Question> oq2 = this.questionRepository.findBySubjectAndContent("스프링부트 모델 질문입니다.", "id는 자동으로 생성되나요?");
        if (oq2.isPresent()) {
            Question q2 = oq2.get();
            assertEquals(lastId, q2.getId());
        }
    }
    
    @Test
    void testJpaFindBySubjectLike() {
        List<Question> qList = this.questionRepository.findBySubjectLike("%sbb%");
        Question q = qList.get(0);
        assertEquals("what is sbb?", q.getSubject());
    }

    @Test
    void testJpaSave() {
        Integer lastId = null;
        Optional<Question> oq1=  this.questionRepository.findFirstByOrderByIdDesc();
        String newSubject = "Spring Boot Model 질문입니다.";
        if (oq1.isPresent()) {
            Question q1 = oq1.get();
            q1.setSubject(newSubject);
            this.questionRepository.save(q1);
            lastId = q1.getId();
        }
        Optional<Question> oq = this.questionRepository.findById(lastId);
        if (oq.isPresent()) {
            Question q = oq.get();
            assertEquals(newSubject, q.getSubject());
        }
    }
    
    @Test
    void testJpaDelete() {
        Question q1 = new Question();
        q1.setSubject("sbb는 무엇인가요?");
        q1.setContent("sbb를 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);
        
        long count = this.questionRepository.count();
        System.out.println(count);
        Integer lastId = null;
        Optional<Question> oq1=  this.questionRepository.findFirstByOrderByIdDesc();
        if (oq1.isPresent()) {
            Question q2 = oq1.get();
            lastId = q2.getId();
        }
        Optional<Question> oq = this.questionRepository.findById(lastId);
        if (oq.isPresent()) {
            Question q = oq.get();
            this.questionRepository.delete(q);
            assertEquals(count - 1, this.questionRepository.count());
        }
    }
}
