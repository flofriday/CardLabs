package simulation

fun generateDeck(): List<Card> {
    var deck = mutableListOf<Card>()

    for (color in listOf(Color.GREEN, Color.BLUE, Color.YELLOW, Color.RED)) {
        // Numberd cards, two of each number-color pairs, except zero where there is only one such pair
        for (number in 0..9) {
            deck.addLast(NumberCard(color, number))
            if (number != 0) {
                deck.addLast(NumberCard(color, number))
            }
        }

        // Two Skip, Switch and Plus Two cards
        for (_i in 1..2) {
            deck.addLast(SkipCard(color))
            deck.addLast(SwitchCard(color))
            deck.addLast(PlusTwoCard(color))
        }

        // FIXME: Add 4x choice cards and 4x Plus 4 choice cards
        for (_i in 1..4) {
        }
    }

    return deck
}
