package simulation

import cardscheme.SchemeError

class DisqualificationError(val botId: Int, val reason: String, schemeError: SchemeError?) : Throwable()
