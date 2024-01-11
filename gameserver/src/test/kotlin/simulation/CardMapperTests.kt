package simulation

import org.junit.Assert
import org.junit.Test
import simulation.models.*

class CardMapper {
    @Test
    fun encodeDecodeAllCards() {
        for (color in listOf(Color.ORANGE, Color.GREEN, Color.PURPLE, Color.CYAN)) {
            for (type in CardType.entries) {
                if (type == CardType.NUMBER_CARD) {
                    for (n in 0..9) {
                        val card = Card(type, color, n)
                        Assert.assertEquals(card, decodeCard(encodeCard(card)))
                    }
                    continue
                }

                val card = Card(type, color, null)
                Assert.assertEquals(card, decodeCard(encodeCard(card)))
            }
        }

        val color = Color.ANY
        for (type in listOf(CardType.CHOOSE, CardType.CHOOSE_DRAW)) {
            val card = Card(type, color, null)
            Assert.assertEquals(card, decodeCard(encodeCard(card)))
        }
    }
}
