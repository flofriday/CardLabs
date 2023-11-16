package at.tuwien.ase.cardlabs.management

class Helper {

    companion object {

        fun requireNull(value: Any?, message: String = "Value is expected to be null") {
            if (value != null) {
                throw IllegalArgumentException(message)
            }
        }

        fun requireNonNull(value: Any?, message: String = "Value is expected to not be null") {
            if (value == null) {
                throw IllegalArgumentException(message)
            }
        }

    }

}