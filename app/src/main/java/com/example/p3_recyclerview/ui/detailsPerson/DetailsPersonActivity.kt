package com.example.p3_recyclerview.ui.detailsPerson

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.p3_recyclerview.databinding.DetailsPersonBinding
import com.example.p3_recyclerview.domain.model.Person
import com.example.p3_recyclerview.domain.usecases.UsecaseGetPerson
import com.example.p3_recyclerview.domain.usecases.UsecaseUpdatePerson
import com.example.p3_recyclerview.domain.usecases.UsecaseValidatedPerson
import com.example.p3_recyclerview.ui.RecyclerActivity
import com.example.p3_recyclerview.utils.StringProvider
import timber.log.Timber

class DetailsPersonActivity : AppCompatActivity() {

    private lateinit var binding: DetailsPersonBinding

    private val viewModel: DetailsPersonViewModel by viewModels {
        DetailsPersonViewModelFactory(
            StringProvider(this),
            UsecaseGetPerson(),
            UsecaseUpdatePerson(),
            UsecaseValidatedPerson(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailsPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        personDetails()
        changeStatus()
        setOnClickListener()
    }

    private fun changeStatus() {
        viewModel.uiState.observe(this@DetailsPersonActivity) { state ->
            val person = state.person
            val message = state.message
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                viewModel.errorMostrado()
                Timber.tag("::TIMBER").w(message)
            }else{
                seePersonDetails(person)
            }
        }
    }

    private fun setOnClickListener() {
        with(binding) {

            btnUpdate.setOnClickListener {
                updatePersonDetails()
            }

            btnBack.setOnClickListener {
                goBack()
            }
        }
    }

    //llamada del ViewModel
    private fun seePersonDetails(personState: Person) {
        val person: Person = personState
        with(binding){
            etNameDetail.editText?.setText(person.name)
            etPasswordDetail.editText?.setText(person.password)
            etPhoneDetail.editText?.setText(person.phone.toString())
        }
    }

    private fun updatePersonDetails(){
        with(binding) {
            val nameNew = etNameDetail.editText?.text.toString()
            val passNew = etPasswordDetail.editText?.text.toString()
            val phoneNew = etPhoneDetail.editText?.text.toString()
            val personNew = Person(nameNew, passNew, phoneNew.toInt())
            viewModel.updatePerson(personNew)
        }
    }

    private fun personDetails(){
        val positionPerson: Int = extraPositionPersonRecyclerActivity()
        viewModel.getPerson(positionPerson)
    }

    private fun extraPositionPersonRecyclerActivity() : Int {
        var positionPerson: Int = -1
        intent.extras?.let {
            positionPerson = it.getInt("positionPerson")
        }
        return positionPerson
    }

    private fun goBack(){
        val intent =  Intent(this@DetailsPersonActivity, RecyclerActivity::class.java)
        startActivity(intent)
    }
}