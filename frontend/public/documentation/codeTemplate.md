You initially start with a default implementation of the `turn` function and empty events for `cardPlayed`, `cardPicked`, and `reshuffledPile`. You can modify the code inside the `turn` function to specify what your bot should do each round. Inside the function, you have access to:

- `topCard` ... the last card that was played. Currently facing up on the discard pile.
- `hand` ... a list of cards that are currently in your hand.
- `players` ... a list of all players.

Furthermore, you can specify you execute code inside the "event" functions, for example, the function `cardPicked` gets called each time when another player picks up a card. If you want, you can react to that event by executing code.

```Scheme
; It's your turn, select a card
; hand is just a list of cards
; topCard is a single card
(define turn (lambda (topCard hand players)
    (random-choice
        (matching-cards topCard hand))))

; Event: cardPlayed
; UnoSaid event is not provided as you can just use cardPlayed
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
```

At the very least, your bot should define the `turn` function and return an appropriate card:

```Scheme
(define (turn topCard hand players)
    <your code goes here>
)
```

A simple example of a bot that filters its hand for all matching cards and then chooses a random one would look like this:

```Scheme
(define (turn topCard hand players)
    (random-choice
        (matching-cards topCard hand)))
```

The cards and players are defined like this:

```Scheme
; Card definition
(define-structure card (color symbol))

; Player definition
(define-structure player (id cardCount))
```
