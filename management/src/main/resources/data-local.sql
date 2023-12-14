INSERT INTO location (name)
VALUES ('Austria');
INSERT INTO location (name)
VALUES ('Germany');
INSERT INTO location (name)
VALUES ('Denmark');
INSERT INTO location (name)
VALUES ('Switzerland');
INSERT INTO location (name)
VALUES ('Netherlands');

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