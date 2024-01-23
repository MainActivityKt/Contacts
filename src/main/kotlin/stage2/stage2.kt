package stage2
import utils.*

data class Contact(var firstName: String, var lastName: String, var number: String?) {
    override fun toString(): String {
        return "$firstName $lastName, ${if (number.isNullOrBlank()) "[no number]" else number}"
    }
}

enum class Option(val value: String) {
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
        while (input != Option.EXIT.value) {
            input = getInput(MENU_stage2)
            when (input) {
                Option.ADD.value -> addNewContact()
                Option.REMOVE.value -> removeContact()
                Option.EDIT.value -> performEdit()
                Option.COUNT.value -> printCount()
                Option.LIST.value -> printAllContacts()
                Option.EXIT.value -> break
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
            print(SELECT_FIELD_PERSON)
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

fun main() {
    PhoneBook().start()
}
