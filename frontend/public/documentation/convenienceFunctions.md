To make the writing proceess of bots easier we provide some helper functions that provide common functionality.

- `matches-card? <card1> <card2>` ... Returns a boolean value if it is allowed that `<card2>` follows `<card1>`.
- `matching-cards <card> <cardList>` ... Returns all cards from `<cardList>` that are allowed to be played, i.e. match, `<card>`.
- `random` ... Returns a random number between 0 and 1.
- `random-choice <arg>` ... Returns a random element from a list or vector that is provided with `<arg>`.
