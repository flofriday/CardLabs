import simulation.Color
import simulation.NumberCard
import simulation.PlusTwoCard
import simulation.encodeCard

fun main(args: Array<String>) {
    println(encodeCard(PlusTwoCard(Color.RED)))
    println(encodeCard(NumberCard(Color.GREEN, 8)))
}
