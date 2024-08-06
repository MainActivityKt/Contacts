package contacts

import contacts.utils.Utils.Contact

const val SUCCESS_MESSAGE = "A record created!\n" + "A Phone Book with a single record created!"

fun getContact(): Contact {
    println("Enter the name of the person:")
    val name: String = readln()
    println("Enter the surname of the person:")
    val surname: String = readln()
    println("Enter the number:")
    val number: String = readln()
    val contact = Contact(name, surname, number)
    return contact
}

fun main(args: Array<String>) {
    val contacts = listOf(getContact())
    println(SUCCESS_MESSAGE)
}