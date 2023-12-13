; The current API for the Bots.

; Card definition
(define-structure card (color symbol))

; Create a card
(make-card "red" "reverse")

; Player definition
(define-structure player (name cardCount))

; It's your turn, select a card
; hand is just a list of cards
; topCard is a single card
(define turn (lambda (topCard hand players)
    (random-choice
        (matching-cards topCard hand))))

; Event: cardPlayed
; UnoSaid event is not provided as the user can just use cardPlayed
(define cardPlayed (lambda (card player)
    ))

; Event: cardPicked
; Some other player had to pick a card
(define cardPicked (lambda (topCard player)
    ))

; Event: reshuffledPile
; The drawPile got reshuffled
(define reshuffledPile (lambda ()
                       ))