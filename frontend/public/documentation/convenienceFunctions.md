To make the writing proceess of bots easier we provide some helper functions that provide common functionality.

- `matches-card? <top-card> <next-card>` ... Returns a boolean value if it is allowed that `<next-card>` follows `<top-card>`.
- `matching-cards <top-card> <cardList>` ... Returns all cards from `<cardList>` that are allowed to be played, i.e. match, `<top-card>`.
- `random` ... Returns a random float between 0 and 1.
- `random-choice <list>` ... Returns a random element from a list or vector that is provided with `<list>`.
