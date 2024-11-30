-- question
INSERT INTO question (subject, content, create_date) VALUES 
('what is sbb.',                'I want to know sbb.',       '2024-11-13 01:06:31'),
('스프링부트 모델 질문입니다.', 'id는 자동으로 생성되나요?', '2024-11-13 01:11:41')  ;

-- answer
INSERT INTO answer (question_q_id, content, create_date) VALUES 
(1, 'sbb is a simple basic board', '2024-11-14 22:06:01')  ;

-- USER
INSERT INTO dho_user (user_id, game_id, user_nm, create_date) VALUES 
('U11', 'wooskyheaven', '문익점', CURRENT_TIMESTAMP()),
('U12', 'wooskyheaven', '멜서스', CURRENT_TIMESTAMP()),
('U23', 'woogreate',    '지인'  , CURRENT_TIMESTAMP()),
('U24', 'woogreate',    '선인'  , CURRENT_TIMESTAMP()),
('U35', 'woogood',      '무위'  , CURRENT_TIMESTAMP()),
('U36', 'woogood',      '누구'  , CURRENT_TIMESTAMP())  ;

-- TO_DO
INSERT INTO to_do (todo_id,user_user_id,content,create_date) VALUES 
(DEFAULT,'U11','수지 개인농장 100단위로 12,000원에 팔기',CURRENT_TIMESTAMP()),
(DEFAULT,'U11','점토 개인농장 100단위로 12,500원에 팔기',CURRENT_TIMESTAMP()),
(DEFAULT,'U23','소금구이 모두 5,600원에 팔기',CURRENT_TIMESTAMP()),
(DEFAULT,'U23','다랑어 올리브 스테이크 모두 23,800원에 팔기',CURRENT_TIMESTAMP()) ;
(DEFAULT,'U23','프프란 트리 모두 199,000원에 팔기',CURRENT_TIMESTAMP()) ;
(DEFAULT,'U24','Sai_Fi_Wh3 개발 중',CURRENT_TIMESTAMP()) ;
