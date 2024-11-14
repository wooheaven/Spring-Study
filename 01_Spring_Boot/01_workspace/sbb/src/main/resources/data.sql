-- question
INSERT INTO question (subject, content, create_date) VALUES 
('what is sbb.',                'I want to know sbb.',       '2024-11-13 01:06:31'),
('스프링부트 모델 질문입니다.', 'id는 자동으로 생성되나요?', '2024-11-13 01:11:41');

-- answer
INSERT INTO answer (question_q_id, content, create_date) VALUES 
(1, 'sbb is a simple basic board', '2024-11-14 22:06:01');

-- member
INSERT INTO dho_user (user_id, game_id, user_nm) VALUES 
('U11', 'wooskyheaven', '문익점'),
('U12', 'wooskyheaven', '멜서스'),
('U23', 'woogreate',    '지인'),
('U24', 'woogreate',    '선인'),
('U35', 'woogood',      '무위'),
('U36', 'woogood',      '누구');