package utils

const val MENU_stage2 = "Enter action (add, remove, edit, count, list, exit): "
const val MENU_stage3 = "Enter action (add, remove, edit, count, list, exit): "
const val ENTER_FIRSTNAME = "Enter the name:"
const val ENTER_LASTNAME = "Enter the surname:"
const val ENTER_BIRTHDAY = "Enter the birth date:"
const val ENTER_NUMBER = "Enter the number:"
const val WRONG_NUMBER = "Wrong number format!"
const val RECORD_ADDED = "The record added."
const val UPDATED_RECORD = "The record updated!"
const val SELECT_RECORD = "Select a record:"
const val SELECT_FIELD_PERSON = "Select a field (name, surname, birth, gender, number):"
const val SELECT_FIELD_ORGANIZATION = "Select a field (address, number):"
const val enterContactType = "Enter the type (person, organization):"
const val ENTER_GENDER = "Enter the gender (M, F):"
const val ENTER_ORG_NAME = "Enter the organization name:"
const val ENTER_ORG_ADDRESS = "Enter the address:"
const val NULL = "[no data]"
const val ENTER_INDEX = "Enter index to show info: "
const val BAD_GENDER = "Bad gender!"
const val BAD_BIRTHDATE = "Bad birth date!"


enum class Gender {
    M, F
}

enum class ContactType(val value: String) {
    PERSON("person"), ORGANIZATION("organization")
}

enum class Field(val value: String) {
    NAME("name"), SURNAME("surname"), BIRTH("birth"),
    GENDER("gender"), NUMBER("number"), ADDRESS("address")
}

fun getInput(message: String): String {
    print("$message ")
    return readln()
}

fun isPhoneNumberValid(input: String): Boolean {
    val firstGroupPattern = Regex("""\+?\(?[0-9A-Za-z]+\)?""")
    val nextGroupsPattern = Regex("""\(?\+?[0-9A-Za-z]{2,}\)?""")
    val separator = (Regex("""[- ]"""))
    val parenthesisCount = input.count { it in "()" }

    input.split(separator).forEachIndexed { index, s ->
        if (parenthesisCount > 2 || s.count { it in "()" } % 2 != 0) return false
        if (index == 0) {
            if(!firstGroupPattern.matches(s)) return false
        }
        else if (!nextGroupsPattern.matches(s)) return false
    }
    return true
}
