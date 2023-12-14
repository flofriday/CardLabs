INSERT INTO location (name, continent)
VALUES ('Austria', 'EUROPE');
INSERT INTO location (name, continent)
VALUES ('Germany', 'EUROPE');
INSERT INTO location (name, continent)
VALUES ('Denmark', 'EUROPE');
INSERT INTO location (name, continent)
VALUES ('Switzerland', 'EUROPE');
INSERT INTO location (name, continent)
VALUES ('Netherlands', 'EUROPE');
INSERT INTO location (name, continent)
VALUES ('Japan', 'ASIA');


-- Password for the accounts: test1:pass1 test2:pass2 test3:pass3 test4:pass4
INSERT INTO ACCOUNT (id, created, deleted, updated, email, password, send_change_updates, send_newsletter, send_score_updates, username, location) VALUES (999999999, TIMESTAMP WITH TIME ZONE '2023-11-24 11:50:04.206298+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:50:04.206298+01', 'test1@email', '$2a$10$ONDpia0apBImGfFBf/4nauIHsoKxgFfluyMHxDfZcslUWUuzrhvA2', TRUE, FALSE, TRUE, 'test1', 'Austria');
INSERT INTO ACCOUNT (id, created, deleted, updated, email, password, send_change_updates, send_newsletter, send_score_updates, username, location) VALUES (999999998, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:00.302842+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:00.302842+01', 'test2@email', '$2a$10$BRpCdEjDwoZMtzFY/p0dlOyu33shFmAgZ.4ufx7LzEEVTfQ7Yhnbm', FALSE, TRUE, TRUE, 'test2', 'Denmark');
INSERT INTO ACCOUNT (id, created, deleted, updated, email, password, send_change_updates, send_newsletter, send_score_updates, username, location) VALUES (999999997, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:23.151055+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:23.151055+01', 'test3@email', '$2a$10$X1MqlGvqvzRCncIsQWmwPeVw3jGpGTbbfA9JmpmDjryZxdepGRpJe', FALSE, TRUE, FALSE, 'test3', 'Japan');
INSERT INTO ACCOUNT (id, created, deleted, updated, email, password, send_change_updates, send_newsletter, send_score_updates, username, location) VALUES (999999996, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:47.996734+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:47.996734+01', 'test4@email', '$2a$10$3OruGPVHedOkqqZOlyFQgeRpYDcOGGb1J87xzZv0GAgFCW/6juLRC', FALSE, FALSE, TRUE, 'test4', 'Austria');

--bots for user 1
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999994, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 1 for user test1', 'CREATED', 'READY', 1000, NULL, 'Neous', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999995, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 2 for user test1', 'CREATED', 'READY', 2000, NULL, 'Raxgios', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999996, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 3 for user test1', 'CREATED', 'READY', 3000, NULL, 'Jenlonos', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999997, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 4 for user test1', 'CREATED', 'READY', 4000, NULL, 'Tynixos', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999998, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 5 for user test1', 'CREATED', 'READY', 5000, NULL, 'Vexgios', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999987, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 6 for user test1', 'CREATED', 'READY', 6000, NULL, 'Zargitron', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999986, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 6 for user test1', 'CREATED', 'READY', 400, NULL, 'Vexphous', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999985, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 6 for user test1', 'CREATED', 'READY', 1200, NULL, 'Tydortron', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999984, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 6 for user test1', 'CREATED', 'READY', 510, NULL, 'Neodoros', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999983, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 6 for user test1', 'CREATED', 'READY', 254, NULL, 'Krygitron', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999982, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 6 for user test1', 'CREATED', 'READY', 789, NULL, 'Vexnixax', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999981, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 6 for user test1', 'CREATED', 'READY', 3510, NULL, 'Xendorphis', 999999999);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999980, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 6 for user test1', 'CREATED', 'READY', 3510, NULL, 'Jenphoos', 999999999);


--bots for user 2
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999993, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 1 for user test2', 'CREATED', 'READY', 1000, NULL, 'Zarzorax', 999999998);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999992, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 2 for user test2', 'CREATED', 'READY', 2000, NULL, 'Zarlonphis', 999999998);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999991, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 3 for user test2', 'CREATED', 'READY', 3000, NULL, 'Jenphoium', 999999998);


--bots for user 3
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999990, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 1 for user test3', 'CREATED', 'READY', 1000, NULL, 'Neophis', 999999997);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999989, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 2 for user test3', 'CREATED', 'READY', 2000, NULL, 'Krylonium', 999999997);
INSERT INTO BOT (id, created, deleted, updated, code_updated, current_code, current_state, default_state, elo_score, error_state_message, name, owner_id) VALUES (999999988, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', NULL, TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', TIMESTAMP WITH TIME ZONE '2023-12-17 22:09:50.591343+01', 'Code of bot 3 for user test3', 'CREATED', 'READY', 3000, NULL, 'Neotarphis', 999999997);

-- Password for the accounts: test1:pass1 test2:pass2 test3:pass3 test4:pass4 test5:pass1
-- Note: if the auto incrementer reaches 10000 it will result in a conflict as it does not know about these values, do not use in production
INSERT INTO ACCOUNT
(id, username, email, password, location, send_change_updates, send_newsletter, send_score_updates, created, updated)
VALUES
    (10000, 'test1', 'test1@email', '$2a$10$ONDpia0apBImGfFBf/4nauIHsoKxgFfluyMHxDfZcslUWUuzrhvA2', 'Austria', TRUE, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10001, 'test2', 'test2@email', '$2a$10$BRpCdEjDwoZMtzFY/p0dlOyu33shFmAgZ.4ufx7LzEEVTfQ7Yhnbm', 'Denmark', FALSE, TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10002, 'test3', 'test3@email', '$2a$10$X1MqlGvqvzRCncIsQWmwPeVw3jGpGTbbfA9JmpmDjryZxdepGRpJe', 'Switzerland', FALSE, TRUE, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10003, 'test4', 'test4@email', '$2a$10$3OruGPVHedOkqqZOlyFQgeRpYDcOGGb1J87xzZv0GAgFCW/6juLRC', 'Austria', FALSE, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10004, 'test5', 'test5@email', '$2a$10$ONDpia0apBImGfFBf/4nauIHsoKxgFfluyMHxDfZcslUWUuzrhvA2', 'Austria', FALSE, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Create a few default bots
INSERT INTO BOT
(id, name, owner_id, current_code, elo_score, current_state, default_state, created)
VALUES
    (10000, 'Neophotron', 10000, '', 1000, 'CREATED', 'READY', CURRENT_TIMESTAMP),
    (10001, 'Neozoros', 10000, '', 1000, 'CREATED', 'READY', CURRENT_TIMESTAMP),
    (10002, 'Zarlonphis', 10000, '', 1000, 'CREATED', 'READY', CURRENT_TIMESTAMP),
    (10003, 'Vextarlar', 10000, '', 1000, 'CREATED', 'READY', CURRENT_TIMESTAMP),
    (10004, 'Xentron', 10000, '', 1000, 'CREATED', 'READY', CURRENT_TIMESTAMP),
    (10005, 'Vexgieon', 10000, '', 1000, 'CREATED', 'READY', CURRENT_TIMESTAMP),
    (10006, 'Krytaros', 10000, '', 1000, 'CREATED', 'READY', CURRENT_TIMESTAMP);