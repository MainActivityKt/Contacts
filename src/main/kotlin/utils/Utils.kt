package contacts.utils



object Utils {
    const val MENU = "Enter action (add, remove, edit, count, list, exit): "

    enum class Option { ADD, REMOVE, EDIT, COUNT, LIST, EXIT }
    enum class Field { NAME, SURNAME, NUMBER }

    data class Contact(var name: String, var surname: String) {
        var number: String? = null
            get() {
                return if (field == null) "[no number]" else field
            }
            set(value) {
                field = if (!value.isNullOrEmpty() && value.isValidPhoneNumber()) {
                    value
                } else {
                    println("Wrong number format!")
                    null
                }
            }

        override fun toString(): String {
            return "$name $surname, $number"
        }
    }

}

fun String.isValidPhoneNumber(): Boolean {
    val firstGroupPattern = Regex("""\+?\(?[0-9A-Za-z]+\)?""")
    val nextGroupsPattern = Regex("""\(?\+?[0-9A-Za-z]{2,}\)?""")
    val separator = (Regex("""[- ]"""))
    val parenthesisCount = this.count { it in "()" }

    this.split(separator).forEachIndexed { index, s ->
        if (parenthesisCount > 2 || s.count { it in "()" } % 2 != 0) return false
        if (index == 0) {
            if(!firstGroupPattern.matches(s)) return false
        }
        else if (!nextGroupsPattern.matches(s)) return false
    }
    return true
}