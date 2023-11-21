class Environment(val enclosing: Environment?, val values: HashMap<String, SchemeValue>) {

    fun isGlobal(): Boolean {
        return enclosing == null
    }

    fun getGlobal(): Environment {
        if (enclosing == null) {
            return this
        }
        return enclosing.getGlobal()
    }

    fun update(name: String, value: SchemeValue) {
        if (values.contains(name)) {
            values.put(name, value)
            return
        }
        if (enclosing == null) {
            throw SchemeError("Variable not found", "I couldn't find any variable with the name '$name'", null, null)
        }
        enclosing.update(name, value)
    }

    fun put(name: String, value: SchemeValue) {
        // FIXME: Research if shadowing must be prevented
        values.put(name, value)
    }

    fun get(name: String): SchemeValue? {
        if (values.contains(name)) {
            return values[name]
        }

        if (enclosing == null) {
            return null
        }

        return enclosing.get(name)
    }

    fun has(name: String): Boolean {
        if (values.contains(name)) {
            return true
        }

        if (enclosing == null) {
            return false
        }

        return enclosing.has(name)
    }
}
