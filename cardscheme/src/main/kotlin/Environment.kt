class Environment(val enclosing: Environment?, val values: HashMap<String, SchemeValue>) {

    fun isGlobal(): Boolean {
        return enclosing == null
    }

    fun put(name: String, value: SchemeValue) {
        // FIXME: Research if shadowing must be prevented
        values.put(name, value)
    }

    fun get(name: String): SchemeValue? {
        if (values.contains(name))
            return values[name]

        if (enclosing == null)
            return null

        return enclosing.get(name)
    }

    fun has(name: String): Boolean {
        if (values.contains(name))
            return true

        if (enclosing == null)
            return false

        return enclosing.has(name)
    }
}