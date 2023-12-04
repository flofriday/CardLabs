package simulation

import org.junit.Test

class CardTests {
    // NumberCards
    @Test
    fun equalNumberCardMatch() {
        val a = NumberCard(Color.RED, 1)
        val b = NumberCard(Color.RED, 1)
        assert(a.match(b))
    }

    @Test
    fun sameColorNumberCardMatch() {
        val a = NumberCard(Color.RED, 1)
        val b = NumberCard(Color.RED, 3)
        assert(a.match(b))
    }

    @Test
    fun samNumberNumberCardMatch() {
        val a = NumberCard(Color.RED, 1)
        val b = NumberCard(Color.BLUE, 1)
        assert(a.match(b))
    }

    @Test
    fun differentNumberCardNotMatch() {
        val a = NumberCard(Color.RED, 1)
        val b = NumberCard(Color.BLUE, 7)
        assert(!a.match(b))
    }

    @Test
    fun sameColorColorCardAndSkipCardMatch() {
        val a = NumberCard(Color.YELLOW, 1)
        val b = SkipCard(Color.YELLOW)
        assert(a.match(b))
    }

    @Test
    fun differentColorColorCardAndSkipCardNotMatch() {
        val a = NumberCard(Color.YELLOW, 1)
        val b = SkipCard(Color.GREEN)
        assert(!a.match(b))
    }

    @Test
    fun sameColorColorCardAndSwitchCardMatch() {
        val a = NumberCard(Color.YELLOW, 1)
        val b = SwitchCard(Color.YELLOW)
        assert(a.match(b))
    }

    @Test
    fun differentColorColorCardAndSwitchCardNotMatch() {
        val a = NumberCard(Color.YELLOW, 1)
        val b = SwitchCard(Color.GREEN)
        assert(!a.match(b))
    }

    @Test
    fun sameColorColorCardAndPlusCardMatch() {
        val a = NumberCard(Color.YELLOW, 1)
        val b = DrawCard(Color.YELLOW)
        assert(a.match(b))
    }

    @Test
    fun differentColorColorCardAndPlusCardNotMatch() {
        val a = NumberCard(Color.YELLOW, 1)
        val b = DrawCard(Color.GREEN)
        assert(!a.match(b))
    }

    @Test
    fun differentColorColorCardAndChooseDrawCardMatch() {
        val a = NumberCard(Color.YELLOW, 1)
        val b = ChooseDrawCard(Color.GREEN)
        assert(a.match(b))
    }

    @Test
    fun differentColorColorCardAndChooseCardMatch() {
        val a = NumberCard(Color.YELLOW, 1)
        val b = ChooseCard(Color.GREEN)
        assert(a.match(b))
    }

    // SwitchCard

    @Test
    fun equalSwitchCardMatch() {
        val a = SwitchCard(Color.YELLOW)
        val b = SwitchCard(Color.YELLOW)
        assert(a.match(b))
    }

    @Test
    fun unequalSwitchCardNotMatch() {
        val a = SwitchCard(Color.YELLOW)
        val b = SwitchCard(Color.BLUE)
        assert(a.match(b))
    }

    @Test
    fun sameColorSwitchCardAndPlusCardMatch() {
        val a = SwitchCard(Color.YELLOW)
        val b = DrawCard(Color.YELLOW)
        assert(a.match(b))
    }

    @Test
    fun differentColorSwitchCardAndPlusCardNotMatch() {
        val a = SwitchCard(Color.GREEN)
        val b = DrawCard(Color.YELLOW)
        assert(!a.match(b))
    }

    @Test
    fun sameColorSwitchCardAndNumberCardMatch() {
        val a = SwitchCard(Color.YELLOW)
        val b = NumberCard(Color.YELLOW, 2)
        assert(a.match(b))
    }

    @Test
    fun differentColorSwitchCardAndNumberCardNotMatch() {
        val a = SwitchCard(Color.GREEN)
        val b = NumberCard(Color.YELLOW, 2)
        assert(!a.match(b))
    }

    // SkipCard
    @Test
    fun equalSkipCardMatch() {
        val a = SkipCard(Color.YELLOW)
        val b = SkipCard(Color.YELLOW)
        assert(a.match(b))
    }

    @Test
    fun unequalSkipCardNotMatch() {
        val a = SkipCard(Color.YELLOW)
        val b = SkipCard(Color.BLUE)
        assert(a.match(b))
    }

    @Test
    fun sameColorSkipCardAndPlusCardMatch() {
        val a = SkipCard(Color.YELLOW)
        val b = DrawCard(Color.YELLOW)
        assert(a.match(b))
    }

    @Test
    fun differentColorSkipCardAndPlusCardNotMatch() {
        val a = SkipCard(Color.GREEN)
        val b = DrawCard(Color.YELLOW)
        assert(!a.match(b))
    }

    @Test
    fun sameColorSkipCardAndNumberCardMatch() {
        val a = SkipCard(Color.YELLOW)
        val b = NumberCard(Color.YELLOW, 2)
        assert(a.match(b))
    }

    @Test
    fun differentColorSkipCardAndNumberCardNotMatch() {
        val a = SkipCard(Color.GREEN)
        val b = NumberCard(Color.YELLOW, 2)
        assert(!a.match(b))
    }

    // Choose cards
    @Test
    fun anyChooseCardMatchesAnyColor() {
        val a = ChooseCard(Color.ANY)
        val b = NumberCard(Color.YELLOW, 2)
        assert(a.match(b))
    }

    @Test
    fun redChooseCardAndBlueNotMatch() {
        val a = ChooseCard(Color.RED)
        val b = NumberCard(Color.BLUE, 0)
        assert(!a.match(b))
    }

    @Test
    fun redChooseCardAndRedNumberCardMatch() {
        val a = ChooseCard(Color.RED)
        val b = NumberCard(Color.RED, 7)
        assert(a.match(b))
    }
}
