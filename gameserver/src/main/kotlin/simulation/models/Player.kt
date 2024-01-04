package simulation.models

import cardscheme.SchemeInterpreter
import simulation.models.Bot
import simulation.models.Card

data class Player(val bot: Bot, val hand: MutableList<Card>, val interpreter: SchemeInterpreter)
