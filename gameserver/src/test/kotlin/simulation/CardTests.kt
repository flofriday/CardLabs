package simulation

import org.junit.Test
import simulation.models.*

class CardTests {
    // NumberCards
    @Test
    fun equalNumberCardMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.ORANGE, 1)
        val b = Card(CardType.NUMBER_CARD, Color.ORANGE, 1)
        assert(a.match(b))
    }

    @Test
    fun sameColorNumberCardMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.ORANGE, 1)
        val b = Card(CardType.NUMBER_CARD, Color.ORANGE, 3)
        assert(a.match(b))
    }

    @Test
    fun samNumberNumberCardMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.ORANGE, 1)
        val b = Card(CardType.NUMBER_CARD, Color.CYAN, 1)
        assert(a.match(b))
    }

    @Test
    fun differentNumberCardNotMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.ORANGE, 1)
        val b = Card(CardType.NUMBER_CARD, Color.CYAN, 7)
        assert(!a.match(b))
    }

    @Test
    fun sameColorColorCardAndSkipCardMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.PURPLE, 1)
        val b = Card(CardType.SKIP, Color.PURPLE, null)
        assert(a.match(b))
    }

    @Test
    fun differentColorColorCardAndSkipCardNotMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.PURPLE, 1)
        val b = Card(CardType.SKIP, Color.GREEN, null)
        assert(!a.match(b))
    }

    @Test
    fun sameColorColorCardAndSwitchCardMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.PURPLE, 1)
        val b = Card(CardType.SWITCH, Color.PURPLE, null)
        assert(a.match(b))
    }

    @Test
    fun differentColorColorCardAndSwitchCardNotMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.PURPLE, 1)
        val b = Card(CardType.SWITCH, Color.GREEN, null)
        assert(!a.match(b))
    }

    @Test
    fun sameColorColorCardAndPlusCardMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.PURPLE, 1)
        val b = Card(CardType.DRAW_TWO, Color.PURPLE, null)
        assert(a.match(b))
    }

    @Test
    fun differentColorColorCardAndPlusCardNotMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.PURPLE, 1)
        val b = Card(CardType.DRAW_TWO, Color.GREEN, null)
        assert(!a.match(b))
    }

    @Test
    fun differentColorColorCardAndChooseDRAW_TWOCardMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.PURPLE, 1)
        val b = Card(CardType.CHOOSE_DRAW, Color.GREEN, null)
        assert(a.match(b))
    }

    @Test
    fun differentColorColorCardAndChooseCardMatch() {
        val a = Card(CardType.NUMBER_CARD, Color.PURPLE, 1)
        val b = Card(CardType.CHOOSE, Color.GREEN, null)
        assert(a.match(b))
    }

    // SwitchCard

    @Test
    fun equalSwitchCardMatch() {
        val a = Card(CardType.SWITCH, Color.PURPLE, null)
        val b = Card(CardType.SWITCH, Color.PURPLE, null)
        assert(a.match(b))
    }

    @Test
    fun unequalSwitchCardNotMatch() {
        val a = Card(CardType.SWITCH, Color.PURPLE, null)
        val b = Card(CardType.SWITCH, Color.CYAN, null)
        assert(a.match(b))
    }

    @Test
    fun sameColorSwitchCardAndPlusCardMatch() {
        val a = Card(CardType.SWITCH, Color.PURPLE, null)
        val b = Card(CardType.DRAW_TWO, Color.PURPLE, null)
        assert(a.match(b))
    }

    @Test
    fun differentColorSwitchCardAndPlusCardNotMatch() {
        val a = Card(CardType.SWITCH, Color.GREEN, null)
        val b = Card(CardType.DRAW_TWO, Color.PURPLE, null)
        assert(!a.match(b))
    }

    @Test
    fun sameColorSwitchCardAndNumberCardMatch() {
        val a = Card(CardType.SWITCH, Color.PURPLE, null)
        val b = Card(CardType.NUMBER_CARD, Color.PURPLE, 2)
        assert(a.match(b))
    }

    @Test
    fun differentColorSwitchCardAndNumberCardNotMatch() {
        val a = Card(CardType.SWITCH, Color.GREEN, null)
        val b = Card(CardType.NUMBER_CARD, Color.PURPLE, 2)
        assert(!a.match(b))
    }

    // SkipCard
    @Test
    fun equalSkipCardMatch() {
        val a = Card(CardType.SKIP, Color.PURPLE, null)
        val b = Card(CardType.SKIP, Color.PURPLE, null)
        assert(a.match(b))
    }

    @Test
    fun unequalSkipCardNotMatch() {
        val a = Card(CardType.SKIP, Color.PURPLE, null)
        val b = Card(CardType.SKIP, Color.CYAN, null)
        assert(a.match(b))
    }

    @Test
    fun sameColorSkipCardAndPlusCardMatch() {
        val a = Card(CardType.SKIP, Color.PURPLE, null)
        val b = Card(CardType.DRAW_TWO, Color.PURPLE, null)
        assert(a.match(b))
    }

    @Test
    fun differentColorSkipCardAndPlusCardNotMatch() {
        val a = Card(CardType.SKIP, Color.GREEN, null)
        val b = Card(CardType.DRAW_TWO, Color.PURPLE, null)
        assert(!a.match(b))
    }

    @Test
    fun sameColorSkipCardAndNumberCardMatch() {
        val a = Card(CardType.SKIP, Color.PURPLE, null)
        val b = Card(CardType.NUMBER_CARD, Color.PURPLE, 2)
        assert(a.match(b))
    }

    @Test
    fun differentColorSkipCardAndNumberCardNotMatch() {
        val a = Card(CardType.SKIP, Color.GREEN, null)
        val b = Card(CardType.NUMBER_CARD, Color.PURPLE, 2)
        assert(!a.match(b))
    }

    // Choose cards
    @Test
    fun anyChooseCardMatchesAnyColor() {
        val a = Card(CardType.CHOOSE, Color.ANY, null)
        val b = Card(CardType.NUMBER_CARD, Color.PURPLE, 2)
        assert(a.match(b))
    }

    @Test
    fun orangeChooseCardAndCYANNotMatch() {
        val a = Card(CardType.CHOOSE, Color.ORANGE, null)
        val b = Card(CardType.NUMBER_CARD, Color.CYAN, 0)
        assert(!a.match(b))
    }

    @Test
    fun orangeChooseCardAndORANGENumberCardMatch() {
        val a = Card(CardType.CHOOSE, Color.ORANGE, null)
        val b = Card(CardType.NUMBER_CARD, Color.ORANGE, 7)
        assert(a.match(b))
    }
}
