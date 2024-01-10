You initially start with an empty `turn` function definition. Here you can insert the code that your bot should execute each round. You have access to:

- `topCard` ... the last card that was played. Currently facing up on the discard pile.
- `hand` ... a list of cards that are currently in your hand.
- `players` ... a list of all players.

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
