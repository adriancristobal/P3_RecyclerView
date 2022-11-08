package com.example.p3_recyclerview.ui.detailsPerson

import com.example.p3_recyclerview.domain.model.Person

data class DetailsPersonState (
    val person: Person = Person(null, null, 0),
    val message: String? = null
        )