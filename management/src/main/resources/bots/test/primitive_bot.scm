(define (turn top-card hand players)
    (random-choice
        (matching-cards top-card hand)))


