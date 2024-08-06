package contacts

import contacts.utils.Utils.Contact
import contacts.utils.Utils.MENU
import contacts.utils.Utils.Option
import contacts.utils.Utils.Field

class PhonebookOrganizer {

    private val phonebook = mutableListOf<Contact>()

    fun startPhonebook() {
        println(MENU)
        var input = readln()
        while (input.uppercase() != Option.EXIT.name) {
            when (input.uppercase()) {
                Option.ADD.name -> addNewContact()
                Option.LIST.name -> printContacts()
                Option.EDIT.name -> editContact()
                Option.COUNT.name -> printCount()
                Option.REMOVE.name -> removeContact()
            }
            println(MENU)
            input = readln()
        }
    }

    private fun getInput(message: String): String {
        print(message)
        return readln()
    }

    private fun addNewContact() {
        val name = getInput("Enter the name: ")
        val surname = getInput("Enter the surname: ")
        val number = getInput("Enter the number: ")
        Contact(name, surname).apply {
            this.number = number
            phonebook.add(this)
        }
        println("The record added.")
    }

    private fun printContacts() {
        phonebook.forEachIndexed { index, contact -> println("${index + 1}. $contact") }
    }

    private fun editContact() {
        if (phonebook.isEmpty()) {
            println("No records to edit!")
        } else {
            printContacts()
            val index = getInput("Select a record: ").toInt()
            val contact = phonebook[index - 1]
            val field = getInput("Select a field (name, surname, number): ")
            when(field.uppercase()) {
                Field.NAME.name -> {
                    val updatedName = getInput("Enter name: ")
                    contact.name = updatedName
                }
                Field.SURNAME.name -> {
                    val updatedSurname = getInput("Enter surname: ")
                    contact.surname = updatedSurname
                }
                Field.NUMBER.name -> {
                    val updatedNumber = getInput("Enter number: ")
                    contact.number = updatedNumber
                }
            }
            println("The record updated!")
        }
    }

    private fun printCount() {
        println("The Phone Book has ${phonebook.size} records.")
    }

    private fun removeContact() {
        if (phonebook.isEmpty()) {
            println("No records to remove!")
        } else {
            printContacts()
            val contactIndex = getInput("Select a record: ").toInt()
            phonebook.removeAt(contactIndex - 1)
        }
    }
}

fun main(args: Array<String>) {
    PhonebookOrganizer().startPhonebook()
}