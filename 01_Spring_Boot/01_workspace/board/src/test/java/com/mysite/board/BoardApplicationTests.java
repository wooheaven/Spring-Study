package com.mysite.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.board.model.Answer;
import com.mysite.board.model.Question;
import com.mysite.board.repository.AnswerRepository;
import com.mysite.board.repository.QuestionRepository;

@SpringBootTest
class BoardApplicationTests {
    @Autowired
    private QuestionRepository qRepo;

    @Autowired
    private AnswerRepository aRepo;

    @Test
    void testJpa() {
//        Inital Question
        List<Question> qList = this.qRepo.findAll();

//        Create/Read Question
        Question q1 = new Question();
        q1.setSubject("여기 board는 무엇인가요?");
        q1.setContent("여기 board에 대해 알고 싶습니다");
        q1.setCreateTime(LocalDateTime.now());
        q1 = this.qRepo.save(q1);

        Question q2 = new Question();
        q2.setSubject("여기 board의 id는 자동생성되는지 알고 싶습니다.");
        q2.setContent("여기 board의 id는 자동생성되는지 알고 싶습니다");
        q2.setCreateTime(LocalDateTime.now());
        q2 = this.qRepo.save(q2);

        qList = this.qRepo.findAll();
        assertEquals(2, qList.size());
        assertEquals("여기 board는 무엇인가요?", qList.get(0).getSubject());

        Optional<Question> qOption = this.qRepo.findById(1L);
        if (qOption.isPresent()) {
            Question q = qOption.get();
            assertEquals("여기 board는 무엇인가요?", qOption.orElse(null).getSubject());
        }
        assertEquals("여기 board는 무엇인가요?", qOption.orElse(null).getSubject());

        qOption = this.qRepo.findBySubject("여기 board의 id는 자동생성되는지 알고 싶습니다.");
        assertEquals(2L, qOption.orElse(null).getId());

        qOption = this.qRepo.findBySubjectAndContent("여기 board의 id는 자동생성되는지 알고 싶습니다.", "여기 board의 id는 자동생성되는지 알고 싶습니다");
        assertEquals(2, qOption.orElse(null).getId());

        qList = this.qRepo.findBySubjectLike("%board%");
        assertEquals(1, qList.get(0).getId());

//        Update Question
        qList.get(1).setContent("자동생성되나요?");
        this.qRepo.save(qList.get(1));
        qList = this.qRepo.findBySubjectLike("%board%");
        assertEquals("자동생성되나요?", qList.get(1).getContent());

//        Delete Question
        Optional<Question> qOptional = this.qRepo.findById(1L);
        Question q = qOptional.get();
        this.qRepo.delete(q);
        assertEquals(1, this.qRepo.count());

//        Create Answer
        qOptional = this.qRepo.findById(2L);
        q = qOptional.get();
        Answer a = new Answer();
        a.setQuestion(q);
        a.setContent("네 자동생성됩니다");
        a.setCreateTime(LocalDateTime.now());
        a = this.aRepo.save(a);

//        Read Answer
        Optional<Answer> aOptional = this.aRepo.findById(1L);
        a = aOptional.orElse(null);
        assertEquals(2, a.getQuestion().getId());
    }

    @Transactional
    @Test
    void testJpa2() {
        Optional<Question> qOptional = this.qRepo.findById(2L);
        Question q = qOptional.get();
        List<Answer> answerList = q.getAnswerList();
        assertEquals(1, answerList.size());
        assertEquals("네 자동생성됩니다", answerList.get(0).getContent());
    }
}
