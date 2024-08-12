package contacts.utils



object Utils {
    const val MENU = "Enter action (add, remove, edit, count, list, exit): "
    const val MENU_EXTENDED = "Enter action (add, remove, edit, count, info, exit) "
    const val ENTER_CONTACT_TYPE = "Enter the type (person, organization): "

    enum class Option (val actions: String = "") {
        MENU("add, list, search, count, exit"),
        SEARCH("[number], back, again"),
        RECORD("edit, delete, menu"),
        LIST("[number], back"),
        BACK, AGAIN, DELETE,ADD, REMOVE, EDIT, COUNT, INFO, EXIT }

    enum class Field { NAME, SURNAME, BIRTH, GENDER, NUMBER, ADDRESS }
    enum class ContactType { PERSON, ORGANIZATION }
    enum class Gender(val letter: String) {
        MALE("M"), FEMALE("F")
    }

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
    fun getInput(message: String): String {
        print(message)
        return readln()
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
