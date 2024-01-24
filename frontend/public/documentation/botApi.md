## Data objects

The bots have to deal with cards and players and therefore it is important how
those object are represented in CardScheme.

### Card objects

A card is a vector of two elements where the first is a symbol representing the
color and the second either an integer for number cards or a symbol of the type
of card.

Possible colors are the symbols `'green 'purple 'orange 'cyan 'any` and the
symbols for action cards are `'switch 'skip 'draw 'choose 'choose-draw`.

Examples:

```scheme
#(green 4)
#(purple draw)
#(any choose-draw)
```

### Player object

A player is a vector of two elements, where the first is the name of the bot as
a string and the second one is the counter of how many cards the bot currently
holds.

Example: `#("Walle" 4)`

## Functions

### Turn Function

The function must return the card the bot wants to play.

It is only invoked when the bot holds at least one card and the function must
return exactly one card.

The function receives three arguments

- `top-card` ... the last card that was played. Currently facing up on the discard pile.
- `hand` ... a list of cards that are currently in your hand.
- `players` ... a list of all players.

Example:

```scheme
(define (turn top-card hand players)
    (random-choice
        (matching-cards top-card hand)))
```

### Card Played Event (optional)

Bots can listen to other bots playing cards.

The function has two parameters:

- `card` ... the card the player just played.
- `player` ... the player that just played the card (their hand counter doesn't include the card).

Example:

```scheme
(define (card-played card player)
    (display "card played")
    (display player)
    (display card))
```

### Card Picked Event (optional)

Sometimes a player doesn't have any matching cards they can play and must pick
a card. With the `card-picked` function bots can listen to this event.

The function has two parameters:

- `top-card` ... the card on the top of the pile.
- `player` ... the player that just picked up one card.

Example:

```scheme
(define (card-picked top-card player)
    (display "card picked")
    (display player)
    (display top-card))
```

### Pile Reshuffled Event (optional)

If the draw pile is depleted the cards from the discard pile (except the top
one) get reshuffled into the draw pile.

The function has no parameters.

Example:

```scheme
(define (pile-reshuffled)
    (display "reshuffles"))
```
