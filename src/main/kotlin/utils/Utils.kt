package utils

const val MENU = "Enter action (add, remove, edit, count, list, exit): "
const val ENTER_FIRSTNAME = "Enter the name:"
const val ENTER_LASTNAME = "Enter the surname:"
const val ENTER_NUMBER = "Enter the number:"
const val WRONG_NUMBER = "Wrong number format!"
const val RECORD_ADDED = "The record added."
const val UPDATED_RECORD = "The record updated!"
const val SELECT_RECORD = "Select a record:"
const val SELECT_FIELD = "Select a field (name, surname, number): "

fun getInput(message: String): String {
    print("$message ")
    return readln()
}

