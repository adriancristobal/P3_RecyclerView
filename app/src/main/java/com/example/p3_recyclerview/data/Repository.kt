package com.example.p3_recyclerview.data

import com.example.p3_recyclerview.domain.model.Person

object Repository {
    private val persons = mutableListOf<Person>()

    init {
        persons.add(Person("Juan", "P8EIDN", 653788991))
        persons.add(Person("Maria", "dkfhi76", 683984578))
        persons.add(Person("Roberto", "H80dgf", 621928291))
        persons.add(Person("Miguel", "hsTI536", 642948579))
        persons.add(Person("Daniel", "HDklg674rt", 600432987))
        persons.add(Person("Lucia", "cdLd4938", 672379207))
        persons.add(Person("Marta", "nnfd498grR", 654789112))
    }

    fun addPerson(person: Person) =
        persons.add(person)


    fun getPersons(): List<Person> {
        return persons
    }

    fun deletePerson(position: Int) =
        persons.remove(persons[position])

    fun getPerson(positionPerson: Int): Person {
        return persons[positionPerson]
    }

    fun updatePerson(personLast: Person, personNew: Person) {
        persons.remove(personLast)
        persons.add(personNew)
    }

}