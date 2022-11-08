package com.example.p3_recyclerview.domain.usecases

import com.example.p3_recyclerview.data.Repository
import com.example.p3_recyclerview.domain.model.Person

class UsecaseAddPerson {

    fun addPerson(person: Person) =
        Repository.addPerson(person)

}