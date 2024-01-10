package simulation

import simulation.models.*

fun generateDeck(): List<Card> {
    val deck = mutableListOf<Card>()

    for (color in listOf(Color.CYAN, Color.GREEN, Color.PURPLE, Color.ORANGE)) {
        // Numbered cards, two of each number-color pairs, except zero where there is only one such pair
        for (number in 0..9) {
            deck.addLast(Card(CardType.NUMBER_CARD, color, number))
            if (number != 0) {
                deck.addLast(Card(CardType.NUMBER_CARD, color, number))
            }
        }

        // Two Skip, Switch and Plus Two cards
        for (_i in 1..2) {
            deck.addLast(Card(CardType.SKIP, color, null))
            deck.addLast(Card(CardType.SWITCH, color, null))
            deck.addLast(Card(CardType.DRAW_TWO, color, null))
        }

        // Add 4x choice cards and 4x Plus 4 choice cards
        for (_i in 1..4) {
            deck.addLast(Card(CardType.CHOOSE, Color.ANY, null))
            deck.addLast(Card(CardType.CHOOSE_DRAW, Color.ANY, null))
        }
    }

    return deck
}
