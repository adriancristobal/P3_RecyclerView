package com.example.p3_recyclerview.ui

import com.example.p3_recyclerview.domain.model.Person

data class RecyclerState (
    val person: Person = Person("null","null",-1),
    val listPerson: List<Person> = ArrayList(),
    val message: String? = null,
    //el showList es para que al iniciar el activity, le des al boton y asi haya un cambio de estado,
    //y te puedo mostar la lista
)