package simulation

import cardscheme.SchemeInterpreter

data class Player(val bot: Bot, val hand: MutableList<Card>, val interpreter: SchemeInterpreter)
