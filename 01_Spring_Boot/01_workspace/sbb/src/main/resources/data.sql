-- site_user
INSERT INTO site_user (id, email, password, username) VALUES
(1, 'wooheaven79@gmail.com', '$2a$10$fxFuRMuVNvSWiehZd1IW9.zXHdSi3riSIyHBsO0SXa.pmU1WfzsrK', 'myuser'),
(2, 'wooheaven85@gmail.com', '$2a$10$fxFuRMuVNvSWiehZd1IW9.zXHdSi3riSIyHBsO0SXa.pmU1WfzsrK', 'myuser2'),
(3, 'wooheaven@naver.com', '$2a$10$fxFuRMuVNvSWiehZd1IW9.zXHdSi3riSIyHBsO0SXa.pmU1WfzsrK', 'myuser3')
;

-- question
INSERT INTO question (subject, content, create_date, modify_date, author_id) VALUES
('what is sbb.', 'I want to know sbb.', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(),1),
(           '2',                   '2', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
(           '3',                   '3', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
(           '4',                   '4', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
(           '5',                   '5', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
(           '6',                   '6', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
(           '7',                   '7', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
(           '8',                   '8', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
(           '9',                   '9', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
(          '10',                  '10', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
(          '11',                  '11', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1)
;

-- answer
INSERT INTO answer (question_question_id, content, create_date, modify_date, author_id) VALUES
(1, 'sbb is a simple basic board 1', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2),
(1, 'sbb is a spring basic board 2', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3),
(1, 'sbb is a simple basic board 3', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2),
(1, 'sbb is a spring basic board 4', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3),
(1, 'sbb is a simple basic board 5', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2),
(1, 'sbb is a spring basic board 6', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3),
(1, 'sbb is a simple basic board 7', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2),
(1, 'sbb is a spring basic board 8', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3),
(1, 'sbb is a simple basic board 9', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2),
(1, 'sbb is a spring basic board 10', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3)
;