INSERT INTO location
    (name, continent)
VALUES
    ('Austria', 'EUROPE'),
    ('Germany', 'EUROPE'),
    ('Denmark', 'EUROPE'),
    ('Switzerland', 'EUROPE'),
    ('Netherlands', 'EUROPE'),
    ('Japan', 'ASIA');

INSERT INTO ACCOUNT
    (id, created, deleted, updated, email, username, location)
VALUES
    (999999999, TIMESTAMP WITH TIME ZONE '2023-11-24 11:50:04.206298+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:50:04.206298+01', 'test1@email',  'test1', 'Austria'),
    (999999998, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:00.302842+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:00.302842+01', 'test2@email', 'test2', 'Denmark'),
    (999999997, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:23.151055+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:23.151055+01', 'test3@email', 'test3', 'Japan'),
    (999999996, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:47.996734+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:47.996734+01', 'test4@email', 'test4', 'Austria'),
    (999999901, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:47.996734+01', NULL, TIMESTAMP WITH TIME ZONE '2023-11-24 11:49:47.996734+01', 'test5@email', 'test5', 'Austria');

-- Note: if the auto incrementer reaches 10000 it will result in a conflict as it does not know about these values, do not use in production
INSERT INTO ACCOUNT
    (id, username, email, location, created, updated)
VALUES
    (10000, '0test1', '0test1@email',  'Austria',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10001, '0test2', '0test2@email',  'Denmark', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10002, '0test3', '0test3@email', 'Switzerland', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10003, '0test4', '0test4@email',  'Austria', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10004, '0test5', '0test5@email', 'Austria', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO BOT VALUES
(999999999, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (display "Bot Neogiax of user test1") (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 1000, NULL, 'Neogiax', 999999999),
(999999998, TIMESTAMP '2024-01-24 02:44:38.135548', NULL, TIMESTAMP '2024-01-24 02:44:38.135548', FALSE, TIMESTAMP '2024-01-24 02:44:38.135548', '(define (turn top-card hand players) (display "Bot Vexnixeon of user test1") (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 1020, NULL, 'Vexnixeon', 999999999),
(999999997, TIMESTAMP '2024-01-24 03:45:38.135548', NULL, TIMESTAMP '2024-01-24 03:45:38.135548', FALSE, TIMESTAMP '2024-01-24 03:45:38.135548', '(define (turn top-card hand players) (display "Bot Raxnixax of user test1") (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 1030, NULL, 'Raxnixax', 999999999),
(999999996, TIMESTAMP '2024-01-24 01:46:38.135548', NULL, TIMESTAMP '2024-01-24 01:46:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (display "Bot Xentaros of user test1") (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 1030, NULL, 'Xentaros', 999999999),

(999999995, TIMESTAMP '2024-01-24 01:47:38.135548', NULL, TIMESTAMP '2024-01-24 01:47:38.135548', FALSE, TIMESTAMP '2024-01-24 01:47:38.135548', '(define (turn top-card hand players) (display "Bot Neodoros of user test2") (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 1025, NULL, 'Neodoros', 999999998),
(999999994, TIMESTAMP '2024-01-24 21:48:38.135548', NULL, TIMESTAMP '2024-01-24 21:48:38.135548', FALSE, TIMESTAMP '2024-01-24 21:48:38.135548', '(define (turn top-card hand players) (display "Bot Krymektron of user test2") (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 1140, NULL, 'Krymektron', 999999998),

(999999993, TIMESTAMP '2024-01-24 20:43:38.135548', NULL, TIMESTAMP '2024-01-24 20:43:38.135548', FALSE, TIMESTAMP '2024-01-24 20:43:38.135548', '(define (turn top-card hand players) (display "Bot Krytarax of user test3") (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 840, NULL, 'Krytarax', 999999997),
(999999992, TIMESTAMP '2024-01-24 14:44:38.135548', NULL, TIMESTAMP '2024-01-24 14:44:38.135548', FALSE, TIMESTAMP '2024-01-24 14:44:38.135548', '(define (turn top-card hand players) (display "Bot Zarlar of user test3") (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 950, NULL, 'Zarlar', 999999997),
(999999991, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548', FALSE, TIMESTAMP '2024-01-24 11:48:38.135548', '(define (turn top-card hand players) (display "Bot Zargitron of user test3") (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 1022, NULL, 'Zargitron', 999999997),

(999999990, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 801, NULL, 'Vexlonphis', 999999996),
(999999989, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 802, NULL, 'Vexlonphis', 999999996),
(999999988, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 803, NULL, 'Vexlonphis', 999999996),
(999999987, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 804, NULL, 'Vexlonphis', 999999996),
(999999986, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 805, NULL, 'Vexlonphis', 999999996),
(999999985, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 806, NULL, 'Vexlonphis', 999999996),
(999999984, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 807, NULL, 'Vexlonphis', 999999996),
(999999983, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 808, NULL, 'Vexlonphis', 999999996),
(999999982, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 809, NULL, 'Vexlonphis', 999999996),
(999999981, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 810, NULL, 'Vexlonphis', 999999996),
(999999980, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 811, NULL, 'Vexlonphis', 999999996),
(999999979, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 812, NULL, 'Vexlonphis', 999999996),
(999999978, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548', FALSE, TIMESTAMP '2024-01-24 21:43:38.135548', '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 'CREATED', 'READY', 813, NULL, 'Vexlonphis', 999999996);


INSERT INTO BOT_CODE VALUES
(999999999, TIMESTAMP '2024-01-24 21:43:38.135548', NULL, TIMESTAMP '2024-01-24 21:43:38.135548',  '(define (turn top-card hand players) (display "Bot Neogiax of user test1") (random-choice (matching-cards top-card hand)))', 999999999),
(999999998, TIMESTAMP '2024-01-24 02:44:38.135548', NULL, TIMESTAMP '2024-01-24 02:44:38.135548',  '(define (turn top-card hand players) (display "Bot Vexnixeon of user test1") (random-choice (matching-cards top-card hand)))', 999999998),
(999999997, TIMESTAMP '2024-01-24 03:45:38.135548', NULL, TIMESTAMP '2024-01-24 03:45:38.135548',  '(define (turn top-card hand players) (display "Bot Raxnixax of user test1") (random-choice (matching-cards top-card hand)))', 999999997),
(999999996, TIMESTAMP '2024-01-24 01:46:38.135548', NULL, TIMESTAMP '2024-01-24 01:46:38.135548',  '(define (turn top-card hand players) (display "Bot Xentaros of user test1") (random-choice (matching-cards top-card hand)))', 999999996),

(999999995, TIMESTAMP '2024-01-24 01:47:38.135548', NULL, TIMESTAMP '2024-01-24 01:47:38.135548',  '(define (turn top-card hand players) (display "Bot Neodoros of user test1") (random-choice (matching-cards top-card hand)))', 999999995),
(999999994, TIMESTAMP '2024-01-24 21:48:38.135548', NULL, TIMESTAMP '2024-01-24 21:48:38.135548',  '(define (turn top-card hand players) (display "Bot Krymektron of user test1") (random-choice (matching-cards top-card hand)))', 999999994),

(999999993, TIMESTAMP '2024-01-24 20:43:38.135548', NULL, TIMESTAMP '2024-01-24 20:43:38.135548',  '(define (turn top-card hand players) (display "Bot Krytarax of user test1") (random-choice (matching-cards top-card hand)))', 999999993),
(999999992, TIMESTAMP '2024-01-24 14:44:38.135548', NULL, TIMESTAMP '2024-01-24 14:44:38.135548',  '(define (turn top-card hand players) (display "Bot Zarlar of user test1") (random-choice (matching-cards top-card hand)))', 999999992),
(999999991, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (display "Bot Zargitron of user test1") (random-choice (matching-cards top-card hand)))', 999999991),

(999999990, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999990),
(999999989, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999989),
(999999988, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999988),
(999999987, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999987),
(999999986, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999986),
(999999985, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999985),
(999999984, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999984),
(999999983, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999983),
(999999982, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999982),
(999999981, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999981),
(999999980, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999980),
(999999979, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999979),
(999999978, TIMESTAMP '2024-01-24 11:48:38.135548', NULL, TIMESTAMP '2024-01-24 11:48:38.135548',  '(define (turn top-card hand players) (random-choice (matching-cards top-card hand)))', 999999978);
