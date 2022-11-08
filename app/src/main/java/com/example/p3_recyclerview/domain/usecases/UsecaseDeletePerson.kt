package com.example.p3_recyclerview.domain.usecases

import com.example.p3_recyclerview.data.Repository

class UsecaseDeletePerson {

    fun deletePerson(position: Int) =
        Repository.deletePerson(position)
}