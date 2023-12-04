package simulation

import org.junit.Assert
import org.junit.Test

class CardMapper {
    @Test
    fun encodeDecodeNumberCard() {
        val card = NumberCard(Color.GREEN, 4)
        Assert.assertEquals(card, decodeCard(encodeCard(card)))
    }

    @Test
    fun encodeDecodeSkipCard() {
        val card = SkipCard(Color.GREEN)
        Assert.assertEquals(card, decodeCard(encodeCard(card)))
    }

    @Test
    fun encodeDecodeSwitchCard() {
        val card = SwitchCard(Color.RED)
        Assert.assertEquals(card, decodeCard(encodeCard(card)))
    }

    @Test
    fun encodeDecodePlusTwoCard() {
        val card = PlusTwoCard(Color.BLUE)
        Assert.assertEquals(card, decodeCard(encodeCard(card)))
    }
}
