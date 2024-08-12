package contacts

import contacts.utils.Utils.ENTER_CONTACT_TYPE
import contacts.utils.Utils.Gender
import contacts.utils.Utils.Field
import contacts.utils.Utils.ContactType
import contacts.utils.Utils.Option
import contacts.utils.Utils.getInput
import contacts.utils.isValidPhoneNumber
import kotlinx.datetime.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import java.io.File
import java.time.DateTimeException

@Serializable
sealed class ContactRecord {
    abstract var name: String
    abstract var number: String?
    abstract val creationDate: Instant
    abstract var lastUpdateDate: Instant

    abstract fun getModifiableFields(): String
    // A method that returns all the possible properties this class is able to change.
    abstract fun changeField(fieldName: String, newValue: String)
    // A method that takes a string that represents a property that the class is able to change and its new value.
    abstract fun getAllFieldsValues(): String
    //A method that returns a string representation of all of  property values together.
}

@Serializable
class PersonContact(
    var firstName: String,
    var surname: String,
    var birthdate: LocalDate?,
    var gender: Gender?,
    override var number: String?,
    override val creationDate: Instant,
    override var lastUpdateDate: Instant = creationDate,
) :
    ContactRecord() {
    override var name = "$firstName $surname"
    override fun getModifiableFields(): String {
        return "name, surname, birth, gender, number"
    }

    override fun changeField(fieldName: String, newValue: String) {
        when(fieldName.uppercase()) {
            Field.NAME.name -> firstName = newValue
            Field.SURNAME.name -> surname = newValue
            Field.BIRTH.name -> {
                try {
                    birthdate = LocalDate.parse(newValue)
                } catch (e: DateTimeException) {
                    println("Bad birth date!")
                    birthdate = null
                }
            }
            Field.GENDER.name -> {
                newValue.apply {
                    gender = if (equals(Gender.MALE.letter)) Gender.MALE else if (equals(Gender.FEMALE.letter)) Gender.FEMALE else {
                        println("Bad gender!")
                        null
                    }
                }
            }
            Field.NUMBER.name -> {
                if (newValue.isValidPhoneNumber()) {
                    number = newValue
                } else {
                    number = null
                    println("Bad number!")
                }
            }
        }
    }

    override fun getAllFieldsValues(): String {
        return listOf(
            firstName, surname, if (number == null )"" else number!!,
            if (birthdate == null) "" else birthdate.toString(), if (gender == null) "" else gender!!.name).joinToString(" ")
    }

    override fun toString(): String {
        return "Name: $firstName\n" +
                "Surname: $surname\n" + "Birth date: ${if (birthdate != null) birthdate else "[no data]"}\n" +
                "Gender: ${if (gender != null) gender else "[no data]"}\n" +
                "Number: ${if (number != null) number else "[no data]"}\n" +
                "Time created: $creationDate\n" + "Time last edit: $lastUpdateDate"
    }
}

@Serializable
class OrganizationContact(
    override var name: String,
    var address: String,
    override var number: String?,
    override val creationDate: Instant,
    override var lastUpdateDate: Instant = creationDate,
) :
    ContactRecord() {
    override fun getModifiableFields(): String {
        return "name, address, number"
    }

    override fun changeField(fieldName: String, newValue: String) {
        when(fieldName.uppercase()) {
            Field.NAME.name -> name = newValue
            Field.ADDRESS.name -> address = newValue
            Field.NUMBER.name -> {
                if (newValue.isValidPhoneNumber()) {
                    number = newValue
                } else {
                    number = null
                    println("Bad number!")
                }
            }
        }
    }

    override fun getAllFieldsValues(): String {
        return listOf(name, if (number == null) "" else number!!, address).joinToString(" ")
    }

    override fun toString(): String {
        return "Organization name: $name\n" + "Address: $address\n" +
                "Number: ${if (number != null) number else "[no data]"}\n" +
                "Time created: $creationDate\n" + "Time last edit: $lastUpdateDate"
    }
}

class AllInOnePhonebook(fileName: String, deleteFileOnExit: Boolean = true) {
    private lateinit var file: File
    private val phoneBook = mutableListOf<ContactRecord>()
    private lateinit var currentMenu: Option

    init {
        file = with(fileName) {
            if (isNullOrEmpty()) File("default.db") else File(this)
        }
       file.apply {
            if (!exists()) {
                createNewFile()
            }
            if (deleteFileOnExit) deleteOnExit()
        }
    }

    private fun updatePhonebook() {
        phoneBook.clear()
        file.readLines().forEach {
            try {
                Json.decodeFromString<PersonContact>(it).apply {
                    phoneBook.add(PersonContact(firstName, surname, birthdate, gender, number, creationDate, lastUpdateDate))
                }
            } catch (e: IllegalArgumentException) {
                Json.decodeFromString<OrganizationContact>(it).apply {
                    phoneBook.add(OrganizationContact(name, address, number, creationDate, lastUpdateDate))
                }
            }
        }
    }

    private fun updateFile() {
        file.writeText("")
        phoneBook.forEach {
            if (it is PersonContact) {
                file.appendText(Json.encodeToString<PersonContact>(it) + "\n")
            } else if (it is OrganizationContact) {
                file.appendText(Json.encodeToString<OrganizationContact>(it) + "\n")
            }
        }
    }

    fun startPhonebook() {
        currentMenu = Option.MENU
        updatePhonebook()
        var input = getAction()
        while (input != Option.EXIT.name) {
            when(input) {
                Option.ADD.name -> addNewContact()
                Option.COUNT.name -> printCount()
                Option.LIST.name -> listContacts()
                Option.SEARCH.name -> performSearch()
            }
            println()
            input = getAction()
        }
    }

    private fun addNewContact() {
        val type = getInput(ENTER_CONTACT_TYPE)
        when (type.uppercase()) {
            ContactType.PERSON.name -> addNewPerson()
            ContactType.ORGANIZATION.name -> addNewOrg()
        }
        println("The record added.")
        updateFile()
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
        phoneBook.add(PersonContact(name, surname, birthDate, gender, number, creationDate, creationDate))
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
        phoneBook.add(OrganizationContact(name, address, number, creationDate))
    }

    private fun printCount() {
        println("The Phone Book has ${phoneBook.size} records.")
    }

    private fun listContacts() {
        phoneBook.forEachIndexed { i, contactRecord -> println("${i + 1}. ${contactRecord.name}") }
        println()
        currentMenu = Option.LIST
        val input = getAction()
        if (input == Option.BACK.name) {
            currentMenu = Option.MENU
        } else {
            viewRecord(input.toInt() - 1)
        }
    }

    private fun viewRecord(index: Int) {
        currentMenu = Option.RECORD
        println(phoneBook[index])
        println()
        val input = getAction()
        when(input) {
            Option.EDIT.name -> editContact(index)
            Option.DELETE.name -> deleteContact(index)
        }
        currentMenu = Option.MENU
    }

    private fun editContact(index: Int) {
        val contact = phoneBook[index]
        val field = getInput("Select a field " + "(${contact.getModifiableFields()}): ")
        contact.changeField(field, getInput("Enter $field: "))
        println("Saved")
        contact.lastUpdateDate = Clock.System.now()
        updateFile()
        viewRecord(index)
    }

    private fun deleteContact(index: Int) {
        phoneBook.removeAt(index)
        updateFile()
        print("Record deleted")
        currentMenu = Option.MENU
    }

    private fun performSearch() {
        currentMenu = Option.SEARCH
        val keyword = getInput("Enter search query: ")
        val results = phoneBook.filter {
            it.getAllFieldsValues().contains(keyword, true)
        }
        println("Found ${results.size} results:")
        results.forEachIndexed { i, contactRecord -> println("${i + 1}. ${contactRecord.name}") }
        println()
        val input = getAction()
        when(input) {
            Option.BACK.name -> currentMenu = Option.MENU
            Option.AGAIN.name -> performSearch()
            else -> {
                val recordIndex = phoneBook.indexOf(results[input.toInt() - 1])
                viewRecord(recordIndex)
            }
        }
    }

    private fun getAction(): String {
        print("[${currentMenu.name.lowercase()}] Enter action (${currentMenu.actions}): ")
        return readln().uppercase()
    }
}

fun main(args: Array<String>) {
    val fileName = if (args.isNotEmpty()) args.last() else ""
    val phonebook = AllInOnePhonebook(fileName)
    phonebook.startPhonebook()
}