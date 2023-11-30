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

-- Password for the accounts: test1:pass1 test2:pass2 test3:pass3 test4:pass4
INSERT INTO ACCOUNT (id, created, deleted, updated, email, password, send_change_updates, send_newsletter, send_score_updates, username, location) VALUES (999999999, TIMESTAMP WITH TIME ZONE '2023-11-24 11:50:04.206298+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:50:04.206298+01', 'test1@email', '$2a$10$ONDpia0apBImGfFBf/4nauIHsoKxgFfluyMHxDfZcslUWUuzrhvA2', TRUE, FALSE, TRUE, 'test1', 'Austria');
INSERT INTO ACCOUNT (id, created, deleted, updated, email, password, send_change_updates, send_newsletter, send_score_updates, username, location) VALUES (999999998, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:00.302842+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:00.302842+01', 'test2@email', '$2a$10$BRpCdEjDwoZMtzFY/p0dlOyu33shFmAgZ.4ufx7LzEEVTfQ7Yhnbm', FALSE, TRUE, TRUE, 'test2', 'Denmark');
INSERT INTO ACCOUNT (id, created, deleted, updated, email, password, send_change_updates, send_newsletter, send_score_updates, username, location) VALUES (999999997, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:23.151055+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:23.151055+01', 'test3@email', '$2a$10$X1MqlGvqvzRCncIsQWmwPeVw3jGpGTbbfA9JmpmDjryZxdepGRpJe', FALSE, TRUE, FALSE, 'test3', 'Switzerland');
INSERT INTO ACCOUNT (id, created, deleted, updated, email, password, send_change_updates, send_newsletter, send_score_updates, username, location) VALUES (999999996, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:47.996734+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:47.996734+01', 'test4@email', '$2a$10$3OruGPVHedOkqqZOlyFQgeRpYDcOGGb1J87xzZv0GAgFCW/6juLRC', FALSE, FALSE, TRUE, 'test4', 'Austria');
