package cardscheme

data class Location(val startline: Int, val endline: Int, val startcol: Int, val endcol: Int) {
    companion object {
        fun merge(
            begin: Location,
            end: Location,
        ): Location {
            val startline = minOf(begin.startline, end.startline)
            var endline = maxOf(begin.endline, end.endline)

            // Assume startcol is from the first location
            var startcol = begin.startcol

            // Now correct if the assumption is wrong
            if (end.startline < begin.startline ||
                (begin.startline == end.startline && end.startcol < begin.startcol)
            ) {
                startcol = end.startcol
            }

            // Next assume the endcol is from the second one
            var endcol = end.endcol

            // Again correct if assumption is wrong
            if (begin.endline > end.endline ||
                (end.endline == begin.endline && begin.endcol > end.endcol)
            ) {
                endcol = begin.endcol
            }

            return Location(startline, startcol, endline, endcol)
        }
    }
}
