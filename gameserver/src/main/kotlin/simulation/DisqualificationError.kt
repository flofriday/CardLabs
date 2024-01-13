package simulation

import cardscheme.SchemeError

class DisqualificationError(val botId: Long, val reason: String, val schemeError: SchemeError?) : Throwable()
