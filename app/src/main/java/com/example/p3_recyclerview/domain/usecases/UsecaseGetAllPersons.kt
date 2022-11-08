package com.example.p3_recyclerview.domain.usecases

import com.example.p3_recyclerview.data.Repository
import com.example.p3_recyclerview.domain.model.Person

class UsecaseGetAllPersons {

    fun getAllPersons(): List<Person> = Repository.getPersons()
}