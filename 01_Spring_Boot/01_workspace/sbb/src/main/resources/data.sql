-- question
INSERT INTO question (subject, content, create_date) VALUES 
('what is sbb.',                'I want to know sbb.',       '2024-11-13 01:06:31'),
('스프링부트 모델 질문입니다.', 'id는 자동으로 생성되나요?', '2024-11-13 01:11:41')  ;

-- answer
INSERT INTO answer (question_q_id, content, create_date) VALUES 
(1, 'sbb is a simple basic board', '2024-11-14 22:06:01')  ;

-- DHO_USER
INSERT INTO dho_user (user_id, game_id, user_nm, create_date) VALUES 
('U11', 'wooskyheaven', '문익점', CURRENT_TIMESTAMP()),
('U12', 'wooskyheaven', '멜서스', CURRENT_TIMESTAMP()),
('U23', 'woogreate',    '지인'  , CURRENT_TIMESTAMP()),
('U24', 'woogreate',    '선인'  , CURRENT_TIMESTAMP()),
('U35', 'woogood',      '무위'  , CURRENT_TIMESTAMP()),
('U36', 'woogood',      '누구'  , CURRENT_TIMESTAMP())  ;

-- DHO_LOGIN
INSERT INTO dho_login (num, left_user_id, right_user_id) VALUES
(1, 'U11', 'U24'),
(2, 'U11', 'U23'),
(3, 'U12', 'U23'),
(4, 'U35', 'U23'),
(5, 'U36', 'U23'),
(6, 'U11', 'U23') ;
UPDATE dho_login AS a
   SET a.left_game_id = (SELECT b.game_id FROM dho_user AS b WHERE b.user_id = a.left_user_id)
     , a.left_user_nm = (SELECT b.user_nm FROM dho_user AS b WHERE b.user_id = a.left_user_id)
 WHERE EXISTS (SELECT * FROM dho_user AS b WHERE b.user_id = a.left_user_id)
;
UPDATE dho_login AS a
   SET a.right_game_id = (SELECT b.game_id FROM dho_user AS b WHERE b.user_id = a.right_user_id)
     , a.right_user_nm = (SELECT b.user_nm FROM dho_user AS b WHERE b.user_id = a.right_user_id)
 WHERE EXISTS (SELECT * FROM dho_user AS b WHERE b.user_id = a.right_user_id)
;

-- TO_DO
INSERT INTO to_do (todo_id,user_user_id,content,create_date) VALUES 
(DEFAULT,'U11','수지 개인농장 100단위로 12,000원에 팔기',               CURRENT_TIMESTAMP()),
(DEFAULT,'U11','점토 개인농장 100단위로 12,500원에 팔기',               CURRENT_TIMESTAMP()),
(DEFAULT,'U11','커민 갯수에 맞춰서 마늘, 코리안더를 U23 에게 전달하기', CURRENT_TIMESTAMP()),
(DEFAULT,'U11','바질 83 을 U23 에게 전달하기',                          CURRENT_TIMESTAMP()),
(DEFAULT,'U11','타임, 로즈마리 모두 U23 에게 전달하기',                 CURRENT_TIMESTAMP()),
(DEFAULT,'U11','정어리 600 유지하기',                                   CURRENT_TIMESTAMP()),
(DEFAULT,'U11','다랑어 50 단위로 U23 에게 전달하기',                    CURRENT_TIMESTAMP()),
(DEFAULT,'U23','바질 83 을 버질리코 스파게티로 만들기',                 CURRENT_TIMESTAMP()),
(DEFAULT,'U23','소금구이 모두 6,200원에 팔기',                          CURRENT_TIMESTAMP()),
(DEFAULT,'U23','다랑어 올리브 스테이크 모두 27,000원에 팔기',           CURRENT_TIMESTAMP()),
(DEFAULT,'U23','프프란 트리 모두 225,000원에 팔기',                     CURRENT_TIMESTAMP()),
(DEFAULT,'U23','합계 Lv116 | 파티쉐로 전직하기',                        CURRENT_TIMESTAMP()),
(DEFAULT,'U23','합계 Lv106 | 모험34 상인63 군인9',                      CURRENT_TIMESTAMP()),
(DEFAULT,'U24','Sai_Fi_Wh3 개발 중',                                    CURRENT_TIMESTAMP()),
(DEFAULT,'U24','U23 플랜트와 똑같이 만들기',                            CURRENT_TIMESTAMP()), 
(DEFAULT,'U24','굴소스, 가람 마사라의 가격을 확인하기',                 CURRENT_TIMESTAMP()), 
(DEFAULT,'U24','조선차, 육두구, 메이스의 가격을 확인하기',              CURRENT_TIMESTAMP()), 
(DEFAULT,'U24','굴소스 가격이 좋으면 굴 짝수 모두 U11 에게 전달하기',   CURRENT_TIMESTAMP()), 
(DEFAULT,'U24','타임 수량만큼 로즈마리를 U11 에게 전달하기',            CURRENT_TIMESTAMP()), 
(DEFAULT,'U24','바질 83 을 U11에게 전달하기',                           CURRENT_TIMESTAMP()), 
(DEFAULT,'U24','정어리 80 을 U11에게 전달하기',                         CURRENT_TIMESTAMP()), 
(DEFAULT,'U24','다랑어 50 단위로 U11 에게 전달하기',                    CURRENT_TIMESTAMP()), 
(DEFAULT,'U24','소금 50, 오레가노 50, 로즈마리 50 런던에서 판매하기',   CURRENT_TIMESTAMP()) ;
