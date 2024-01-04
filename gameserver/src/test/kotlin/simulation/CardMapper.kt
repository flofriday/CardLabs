package simulation

import org.junit.Assert
import org.junit.Test
import simulation.models.*

class CardMapper {
    @Test
    fun encodeDecodeAllCards() {
        for (color in Color.entries) {
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
    }
}
