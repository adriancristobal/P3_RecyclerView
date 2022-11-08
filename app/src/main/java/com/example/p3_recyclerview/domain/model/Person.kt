package com.example.p3_recyclerview.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(val name: String?, val password: String?, val phone: Int) : Parcelable
