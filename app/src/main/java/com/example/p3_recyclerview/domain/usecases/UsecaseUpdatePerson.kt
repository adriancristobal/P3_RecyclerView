package com.example.p3_recyclerview.domain.usecases

import com.example.p3_recyclerview.data.Repository
import com.example.p3_recyclerview.domain.model.Person

class UsecaseUpdatePerson {

    fun updatePerson(pLast: Person, pNew: Person) = Repository.updatePerson(pLast, pNew)
}