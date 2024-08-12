package contacts

import contacts.utils.Utils.ENTER_CONTACT_TYPE
import contacts.utils.Utils.Option
import contacts.utils.Utils.ContactType
import contacts.utils.Utils.Gender
import contacts.utils.Utils.MENU_EXTENDED
import contacts.utils.Utils.Field
import contacts.utils.isValidPhoneNumber
import kotlinx.datetime.*
import java.time.DateTimeException

abstract class Contact(
    open var name: String,
    open var number: String?,
    open val creationDate: Instant,
    open var lastUpdateDate: Instant,
)

data class Person(
    var firstName: String,
    var surname: String,
    var birthdate: LocalDate?,
    var gender: Gender?,
    override var number: String?,
    override val creationDate: Instant,
    override var lastUpdateDate: Instant = creationDate,
) :
    Contact("$firstName $surname", number, creationDate, lastUpdateDate) {
    override fun toString(): String {
        return "Name: $firstName\n" +
                "Surname: $surname\n" + "Birth date: ${if (birthdate != null) birthdate else "[no data]"}\n" +
                "Gender: ${if (gender != null) gender else "[no data]"}\n" +
                "Number: ${if (number != null) number else "[no data]"}\n" +
                "Time created: $creationDate\n" + "Time last edit: $lastUpdateDate"
    }
}

data class Organization(
    override var name: String,
    var address: String,
    override var number: String?,
    override val creationDate: Instant,
    override var lastUpdateDate: Instant = creationDate,
) : Contact(name, number, creationDate, lastUpdateDate) {
    override fun toString(): String {
        return "Organization name: $name\n" + "Address: $address\n" +
                "Number: ${if (number != null) number else "[no data]"}\n" +
                "Time created: $creationDate\n" + "Time last edit: $lastUpdateDate"
    }
}

class PunctualPhonebookOrganizer() {
    private val phonebook = mutableListOf<Contact>()

    fun startPhonebook() {
        println(MENU_EXTENDED)
        var input = readln()
        while (input.uppercase() != Option.EXIT.name) {
            when (input.uppercase()) {
                Option.ADD.name -> addNewContact()
                Option.INFO.name -> showInfo()
                Option.EDIT.name -> editContact()
                Option.COUNT.name -> printCount()
                Option.REMOVE.name -> removeContact()
            }
            println()
            println(MENU_EXTENDED)
            input = readln()
        }
    }

    private fun getInput(message: String): String {
        print(message)
        return readln()
    }

    private fun addNewContact() {
        val type = getInput(ENTER_CONTACT_TYPE)
        when (type.uppercase()) {
            ContactType.PERSON.name -> addNewPerson()
            ContactType.ORGANIZATION.name -> addNewOrg()
        }
        println("The record added.")
    }

    private fun addNewPerson() {
        val name = getInput("Enter the name: ")
        val surname = getInput("Enter the surname: ")
        val birthDate: LocalDate?
        getInput("Enter the birth date: ").apply {
            birthDate = try {
                LocalDate.parse(this)
            } catch (e: Exception) {
                println("Bad birth date!")
                null
            }
        }
        val gender: Gender?
        getInput("Enter the gender (M, F): ").apply {
            gender = if (equals(Gender.MALE.letter)) Gender.MALE else if (equals(Gender.FEMALE.letter)) Gender.FEMALE else {
                println("Bad gender!")
                null
            }
        }
        val number: String?
        getInput("Enter the number: ").apply {
            number = if (isValidPhoneNumber()) this else {
                println("Bad number!")
                null
            }
        }
        val creationDate = Clock.System.now()
        phonebook.add(Person(name, surname, birthDate, gender, number, creationDate, creationDate))
    }

    private fun addNewOrg() {
        val name = getInput("Enter the organization name: ")
        val address = getInput("Enter the address: ")
        val number: String?
        getInput("Enter the number: ").apply {
            number = if (isValidPhoneNumber()) this else {
                println("Bad number!")
                null
            }
        }
        val creationDate = Clock.System.now()
        phonebook.add(Organization(name, address, number, creationDate))
    }

    private fun showInfo() {
        printPhonebook()
        val index = getInput("Enter index to show info: ").toInt()
        println(phonebook[index - 1])
    }

    private fun printPhonebook() {
        phonebook.forEachIndexed { index, contact ->  println("${index + 1}. ${contact.name}")}
    }

    private fun editContact() {
        if (phonebook.isEmpty()) {
            println("No records to edit!")
        } else {
            printPhonebook()
            val index = getInput("Select a record: ").toInt() - 1
            val contact = phonebook[index]
            if (contact is Person ) {
                editPerson(index)
            } else if (contact is Organization) {
                editOrg(index)
            }
            contact.lastUpdateDate = Clock.System.now()
            println("The record updated!")
        }
    }

    private fun editPerson(index: Int) {
        val person = phonebook[index] as Person
        val field = getInput("Select a field (name, surname, birth, gender, number): ")
        when(field.uppercase()) {
            Field.NAME.name -> {
                val newName = getInput("Enter name: ")
                person.firstName = newName
            }
            Field.SURNAME.name -> {
                val updatedSurname = getInput("Enter surname: ")
                person.surname = updatedSurname
            }
            Field.BIRTH.name -> {
                val updatedBirthday = getInput("Enter birth date: ")
                try {
                    person.birthdate = LocalDate.parse(updatedBirthday)
                } catch (e: DateTimeException) {
                    println("Bad birth date!")
                    person.birthdate = null
                }
            }
            Field.GENDER.name -> {
                getInput("Enter the gender (M, F): ").apply {
                    val gender = if (equals(Gender.MALE.letter)) Gender.MALE else if (equals(Gender.FEMALE.letter)) Gender.FEMALE else {
                        println("Bad gender!")
                        null
                    }
                    person.gender = gender
                }


            }
            Field.NUMBER.name -> {
                val updatedNumber = getInput("Enter number: ")
                if (updatedNumber.isValidPhoneNumber()) {
                    person.number = updatedNumber
                } else {
                    person.number = null
                    println("Bad number!")
                }
            }
        }
    }

    private fun editOrg(index: Int) {
        val org = phonebook[index] as Organization
        val field = getInput("Select a field (address, number): ")
        when(field.uppercase()) {
            Field.ADDRESS.name -> {
                val updatedAddress = getInput("Enter address: ")
                org.address = updatedAddress
            }
            Field.NUMBER.name -> {
                val updatedNumber = getInput("Enter number: ")
                if (updatedNumber.isValidPhoneNumber()) {
                    org.number = updatedNumber
                } else {
                    org.number = null
                    println("Bad number!")
                }
            }
        }
    }

    private fun printCount() {
        println("The Phone Book has ${phonebook.size} records.")
    }

    private fun removeContact() {
        if (phonebook.isEmpty()) {
            println("No records to remove!")
        } else {
            printPhonebook()
            val contactIndex = getInput("Select a record: ").toInt()
            phonebook.removeAt(contactIndex - 1)
        }
    }
}

fun main() {
    println("3".uppercase())
    PunctualPhonebookOrganizer().startPhonebook()
}