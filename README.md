# Contacts

In this project, the program will be able to create contacts
(like on the mobile phone) and search for people or organizations by name.

## Stage 1/4 :
In the first stage, the program creates an
instance of a class that stores information about one record in the Contacts.

One record should contain a name, a surname, and a phone number, which the user type them
from the keyboard.

[Open stage 1 on Hyperskill](https://hyperskill.org/projects/261/stages/1321/implement)

Stage implementation: [SingleContact.kt](src/main/kotlin/SingleContact.kt)

Example:

    Enter the name of the person:
    > John
    Enter the surname of the person:
    > Smith
    Enter the number:
    > 1-234-567-890

    A record created!
    A Phone Book with a single record created!

## Stage 2/4: Create a menu

Sometimes we need to restrict the ability to change the instance properties. For example,
a phone number can't be just any string; it should follow some rules.
The phone number format is different for every country, but they all have some elements in common.

- The phone number should be split into groups using a space or dash. One group is also possible.
- Before the first group, there may or may not be a plus symbol.

- The first group or the second group can be wrapped in parentheses, but there should be no
  more than one group that is wrapped in parentheses. There may also be no groups wrapped in parentheses.

- A group can contain numbers, uppercase, and lowercase English letters. A group should be
  at least 2 symbols in length. But the first group may be only one symbol in length.

In this stage, the program keeps all the records in a list.
The user is able to add, remove, edit the records, and get the number of records.

If the user inputs an incorrect phone number, it is set as empty. If the number is empty, write the string `no number` is written instead of it.

[Open stage 2 on Hyperskill](https://hyperskill.org/projects/261/stages/1322/implement)

Stage implementation: [PhonebookOrganizer.kt](src/main/kotlin/PhonebookOrganizer.kt)

    Example:
    Enter action (add, remove, edit, count, list, exit): > add
    Enter the name: > John
    Enter the surname: > Smith
    Enter the number: > +0 (123) 456-789-ABcd
    The record added.
    Enter action (add, remove, edit, count, list, exit): > edit
    1. John Smith, +0 (123) 456-789-ABcd
    Select a record: > 1
    Select a field (name, surname, number): > number
    Enter number: > ()()
    Wrong number format!
    The record updated!
    Enter action (add, remove, edit, count, list, exit): > list
    1. John Smith, [no number]
    Enter action (add, remove, edit, count, list, exit): > exit

## Stage 3/4: Upgrade the contacts
In this stage:
- The program is expanded to store information not only about people, but also organizations.
- Two more properties are added to keep track of creation date and last modfied date and time, for each contact.
- A base class is created, containing information relevant to both Person and Organization contacts, such as name, phone number, and dates.
- Two classes inheriting this base class are created, for Persons and Organizations, containing the unique attributes to each field, practising **inheritance**.

[Open stage 3 on Hyperskill](https://hyperskill.org/projects/261/stages/1323/implementt)

Stage implementation: [PunctualPhonebookOrganizer.kt](src/main/kotlin/PunctualPhonebookOrganizer.kt)

Example:

    Enter action (add, remove, edit, count, info, exit): > add
    Enter the type (person, organization): > person
    Enter the name: > John
    Enter the surname: > Smith
    Enter the birth date: >
    Bad birth date!
    Enter the gender (M, F): >
    Bad gender!
    Enter the number: > +0 (123) 456-789-ABcd
    The record added.
    
    Enter action (add, remove, edit, count, info, exit): > add
    Enter the type (person, organization): > organization
    Enter the organization name: > Pizza Shop
    Enter the address: > Wall St. 1
    Enter the number: > +0 (123) 456-789-9999
    The record added.
              
    Enter action (add, remove, edit, count, info, exit): > info
    1. John Smith
    2. Pizza Shop
    Enter index to show info: > 2
    Organization name: Pizza shop
    Address: Wall St. 1
    Number: +0 (123) 456-789-9999
    Time created: 2018-01-01T00:00
    Time last edit: 2018-01-01T00:00
    
    Enter action (add, remove, edit, count, info, exit): > edit
    1. John Smith
    2. Pizza Shop
    Select a record: > 1
    Select a field (name, surname, birth, gender, number): > number
    Enter number: > (123) 234 345-456
    The record updated!
    
    Enter action (add, remove, edit, count, info, exit): > info
    1. John Smith
    2. Pizza Shop
    Select a record: > 1
    Name: John
    Surname: Smith
    Birth date: [no data]
    Gender: [no data]
    Number: (123) 234 345-456
    Time created: 2018-01-01T00:00
    Time last edit: 2018-01-01T00:01
    
    Enter action (add, remove, edit, count, info, exit): > edit
    1. John Smith
    2. Pizza Shop
    Select a record: > 2
    Select a field (address, number): > address
    Enter address: > Wall St. 7
    The record updated!
    
    Enter action (add, remove, edit, count, info, exit): > info
    1. John Smith
    2. Pizza Shop
    Enter index to show info: > 2
    Organization name: Pizza shop
    Address: Wall St. 7
    Number: +0 (123) 456-789-9999
    Time created: 2018-01-01T00:00
    Time last edit: 2018-01-01T00:02
    
    Enter action (add, remove, edit, count, info, exit): > exit
