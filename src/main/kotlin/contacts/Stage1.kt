package contacts

/*
Description
Contacts is a handy app to have. It stores all of your saved contacts. In this project, you will write one yourself. It teaches you to understand and implement the basic principles of OOP.

In the first stage, you should write a program that creates an instance of a class that stores information about one record in the Contacts. One record should contain a name, a surname, and a phone number. You can type them from the keyboard.

You should also create a class representing this app. For now, it should store only one record: a record that you created from the input.

Example
Below is an example of how your output might look. Lines that start with > represent the user input.

    Enter the name of the person:
    > John
    Enter the surname of the person:
    > Smith
    Enter the number:
    > 1-234-567-890

    A record created!
    A Phone Book with a single record created!
 */

const val SUCCESS_MESSAGE = "A record created!\nA Phone Book with a single record created!"

@Suppress("UNUSED_VARIABLE")
fun main() {
    val name = getInput("Enter the name of the person:")
    val lastName = getInput("Enter the surname of the person:")
    val phoneNumber = getInput("Enter the number:")
    println()
    println(SUCCESS_MESSAGE)
}