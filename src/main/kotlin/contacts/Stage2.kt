package contacts

const val MENU = "Enter action (add, remove, edit, count, list, exit): "
const val ENTER_FIRSTNAME = "Enter the name:"
const val ENTER_LASTNAME = "Enter the surname:"
const val ENTER_NUMBER = "Enter the number:"
const val WRONG_NUMBER = "Wrong number format!"
const val RECORD_ADDED = "The record added."
const val UPDATED_RECORD = "The record updated!"
const val SELECT_RECORD = "Select a record:"
const val SELECT_FIELD = "Select a field (name, surname, number): "

data class Contact(var firstName: String, var lastName: String, var number: String?) {
    override fun toString(): String {
        return "$firstName $lastName, ${if (number.isNullOrBlank()) "[no number]" else number}"
    }
}

enum class Options(val value: String) {
    ADD("add"),
    REMOVE("remove"),
    EDIT("edit"),
    COUNT("count"),
    LIST("list"),
    EXIT("exit")
}

class PhoneBook {
    private val contacts = mutableListOf<Contact>()

    private fun addNewContact() {
        Contact("", "", "").apply {
            updateContactFirstname(this)
            updateContactLastname(this)
            updateContactNumber(this)
            contacts.add(this)
        }
        println(RECORD_ADDED)
    }

    private fun updateContactFirstname(contact: Contact) {
        val firstName = getInput(ENTER_FIRSTNAME)
        contact.firstName = firstName
    }

    private fun updateContactLastname(contact: Contact) {
        val lastName = getInput(ENTER_LASTNAME)
        contact.lastName = lastName
    }

    private fun updateContactNumber(contact: Contact) {
        val number = getInput(ENTER_NUMBER)
        if (isPhoneNumberValid(number)) {
            contact.number = number
        } else {
            println(WRONG_NUMBER)
            contact.number = null
        }
    }

    private fun removeContact() {
        if (contacts.isEmpty()) {
            println("No records to remove!")
        } else {
            printAllContacts()
            print(SELECT_RECORD)
            val id = readln().toInt()
            contacts.removeAt(id - 1)
            println("The record removed!")
        }
    }

    fun start() {
        var input = ""
        while (input != Options.EXIT.value) {
            input = getInput(MENU)
            when (input) {
                Options.ADD.value -> addNewContact()
                Options.REMOVE.value -> removeContact()
                Options.EDIT.value -> performEdit()
                Options.COUNT.value -> printCount()
                Options.LIST.value -> printAllContacts()
                Options.EXIT.value -> break
            }
        }
    }

    private fun printCount() {
        println("The Phone Book has ${contacts.size} records.")
    }

    private fun performEdit() {
        if (contacts.isEmpty()) {
            println("No records to edit!")
        } else {
            printAllContacts()
            print(SELECT_RECORD)
            val id = readln().toInt()
            print(SELECT_FIELD)
            val field = readln()
            println()
            updateContact(id, field)

        }
    }

    private fun updateContact(id: Int, field: String) {
        val contact = contacts[id - 1]
        when (field.lowercase()) {
            "name" -> updateContactFirstname(contact)
            "surname" -> updateContactLastname(contact)
            "number" -> updateContactNumber(contact)
        }
        println(UPDATED_RECORD)
    }

    private fun printAllContacts() {
        contacts.forEachIndexed { i, contact ->
            println("${i + 1}. $contact")
        }
    }
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

fun main() {
    PhoneBook().start()
}
