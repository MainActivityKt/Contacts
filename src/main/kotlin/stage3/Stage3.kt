package stage3
import utils.*
import java.time.LocalDate
import java.time.LocalDateTime

data class Person(
    var firstName: String,
    var lastName: String,
    var dateOfBirth: LocalDate?,
    var gender: Gender?,
    override var number: String?,
    override val creationTime: LocalDateTime,
    override var updateTime: LocalDateTime
): Contact(number, creationTime, updateTime) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}

data class Organization(
    val name: String,
    var address: String,
    override var number: String?,
    override val creationTime: LocalDateTime,
    override var updateTime: LocalDateTime

): Contact(number, creationTime, updateTime) {
    override fun toString(): String {
        return name
    }
}


abstract class Contact(
    open var number: String?,
    open val creationTime: LocalDateTime,
    open var updateTime: LocalDateTime)

enum class Option(val value: String) {
    ADD("add"), REMOVE("remove"), EDIT("edit"),
    COUNT("count"), INFO("info"), EXIT("exit")
}

class Phonebook {
    private val contacts = mutableListOf<Contact>()

    private fun addNewContact() {
        val contactType = getInput(enterContactType)
        if (contactType == ContactType.PERSON.value) addNewPerson() else addNewOrganization()
        println(RECORD_ADDED)
    }

    private fun addNewPerson() {
        val firstName = getInput(ENTER_FIRSTNAME)
        val lastName = getInput(ENTER_LASTNAME)
        val birthDate: LocalDate?
        getInput(ENTER_BIRTHDAY).apply {
            birthDate = try {
                LocalDate.parse(this)
            } catch (e: Exception) {
                println(BAD_BIRTHDATE)
                null
            }
        }
        val gender: Gender?
        getInput(ENTER_GENDER).apply {
            gender = if (this == Gender.M.name) Gender.M else if (this == Gender.F.name) Gender.F else {
                println(BAD_GENDER)
                null
            }
        }
        var number: String?
        getInput(ENTER_NUMBER).apply {
            number = if (isPhoneNumberValid(this)) this else {
                println(WRONG_NUMBER)
                null
            }
        }
        val creationTime = LocalDateTime.now()
        contacts.add(Person(firstName, lastName, birthDate, gender, number, creationTime, creationTime))
    }

    private fun addNewOrganization() {
        val name = getInput(ENTER_ORG_NAME)
        val address = getInput(ENTER_ORG_ADDRESS)
        val number = getInput(ENTER_NUMBER)
        val creationTime = LocalDateTime.now()
        contacts.add(Organization(name, address, number, creationTime, creationTime))
    }

    private fun editContact(contact: Contact) {
        val fieldToEdit = getInput(if (contact is Person) SELECT_FIELD_PERSON else SELECT_FIELD_ORGANIZATION)
        if (contact is Person) {
            when(fieldToEdit) {
                Field.NAME.value -> contact.firstName = getInput(ENTER_FIRSTNAME)
                Field.SURNAME.value -> contact.lastName = getInput(ENTER_LASTNAME)
                Field.BIRTH.value -> {
                    try {
                        contact.dateOfBirth = LocalDate.parse(getInput(ENTER_BIRTHDAY))
                    } catch (e: Exception) {
                        println(BAD_BIRTHDATE)
                        contact.dateOfBirth = null
                    }
                }
                Field.GENDER.value -> {
                    getInput(ENTER_GENDER).apply { contact.gender =
                        if (this == Gender.M.name) Gender.M else if(this == Gender.F.name) Gender.F else {
                            println(BAD_GENDER)
                            null
                        }
                    }
                }
                Field.NUMBER.value -> {
                    getInput(ENTER_NUMBER).apply {contact.number =
                        if (isPhoneNumberValid(this)) this
                        else {
                            println(WRONG_NUMBER)
                            null
                        }
                    }
                }
            }
        } else if (contact is Organization) {
            when(fieldToEdit) {
                Field.ADDRESS.value -> contact.address = getInput(ENTER_ORG_ADDRESS)
                Field.NUMBER.value -> {
                    val newNumber = getInput(ENTER_NUMBER)
                    contact.number = if (isPhoneNumberValid(newNumber)) newNumber else {
                        println(WRONG_NUMBER)
                        null
                    }
                }
            }
        }
        updateLastEdit(contact)
    }

    private fun updateLastEdit(contact: Contact) {
        contact.updateTime = LocalDateTime.now()
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
            input = getInput(MENU_stage3)
            when (input) {
                Option.ADD.value -> addNewContact()
                Option.REMOVE.value -> removeContact()
                Option.EDIT.value -> performEdit()
                Option.COUNT.value -> printCount()
                Option.INFO.value -> getInfo()
                Option.EXIT.value -> break
            }
            println()
        }
    }

    private fun getInfo() {
        printAllContacts()
        val id = getInput(ENTER_INDEX).toInt()
        val contact = contacts[id - 1]
        printContactInfo(contact)
    }

    private fun printContactInfo(contact: Contact) {
        if (contact is Person) {
            println("Name: ${contact.firstName}")
            println("Surname: ${contact.lastName}")
            println("Birth date: ${if (contact.dateOfBirth != null)contact.dateOfBirth else NULL}")
            println("Gender: ${if (contact.gender != null) contact.gender else NULL}")

        } else if (contact is Organization){
            println("Organization name: ${contact.name}")
            println("Address: ${contact.address}")
        }
        println("Number: ${if(contact.number != null) contact.number else NULL}")
        println("Time created: ${contact.creationTime}")
        println("Time last edit: ${contact.updateTime}")
    }
    private fun printCount() {
        println("The Phone Book has ${contacts.size} records.")
    }

    private fun printAllContacts() {
        contacts.forEachIndexed { i, contact ->
            println("${i + 1}. $contact")
        }
    }

    private fun performEdit() {
        if (contacts.isEmpty()) {
            println("No records to edit!")
        } else {
            printAllContacts()
            print(SELECT_RECORD)
            val id = readln().toInt()
            editContact(contacts[id - 1])
            println(UPDATED_RECORD)
        }
    }
}

fun main() {
    Phonebook().start()
}