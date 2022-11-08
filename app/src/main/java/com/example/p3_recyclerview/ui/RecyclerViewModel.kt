package com.example.p3_recyclerview.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.p3_recyclerview.R
import com.example.p3_recyclerview.domain.model.Person
import com.example.p3_recyclerview.domain.usecases.*
import com.example.p3_recyclerview.utils.StringProvider

class RecyclerViewModel (
    private val stringProvider: StringProvider,
    private val getAllPersonsUC: UsecaseGetAllPersons,
    private val getPersonUC: UsecaseGetPerson,
    private val deletePersonUC: UsecaseDeletePerson,
    private val addPersonUC: UsecaseAddPerson,
    private val validatedPersonUC: UsecaseValidatedPerson,
        ) : ViewModel() {

        private val _uiState = MutableLiveData<RecyclerState>()
        val uiState: LiveData<RecyclerState> get() = _uiState

        fun getAllPersons() {
            val listPersonUC = getAllPersonsUC.getAllPersons()
            _uiState.value = _uiState.value?.copy(
                listPerson = listPersonUC
            )
        }

        fun getPerson(positionPerson: Int) {
            if (positionPerson >= getAllPersonsUC.getAllPersons().size || positionPerson < 0){
                val personUC: Person = getPersonUC.getPerson(positionPerson)
                _uiState.value = _uiState.value?.copy(
                    person = personUC
                )
            } else {
                _uiState.value = _uiState.value?.copy(
                    message = stringProvider.getString(R.string.errorGetPerson)
                )
            }
        }

        fun deletePerson(positionPerson: Int) {
            if (!deletePersonUC.deletePerson(positionPerson)) {
                _uiState.value = _uiState.value?.copy(
                    message = stringProvider.getString(R.string.errorDeletePerson)
                )
            } else {
                _uiState.value = _uiState.value?.copy(
                    listPerson = getAllPersonsUC.getAllPersons()
                )
            }
        }

        fun addPerson(p : Person){
            if (validatedPersonUC.validatedPerson(p)) {
                if (!addPersonUC.addPerson(p)) {
                    _uiState.value = _uiState.value?.copy(
                        message = stringProvider.getString(R.string.errorAddPerson)
                    )
                } else {
                    _uiState.value = _uiState.value?.copy(
                        listPerson = getAllPersonsUC.getAllPersons(),
                        message = stringProvider.getString(R.string.niceAddedPerson)
                    )
                }
            } else {
                _uiState.value = _uiState.value?.copy(
                    //person = pLast,
                    message =  stringProvider.getString(R.string.errorValidatePerson)
                )
            }
        }

        fun showList() {
            _uiState.value = RecyclerState(
                listPerson = getAllPersonsUC.getAllPersons(),
            )
        }

        fun errorMostrado() {
            //esta funcion se llama en el observe para dejar el mensaje a null de nuevo y no se quede por defecto el ultimo mensaje
            _uiState.value = _uiState.value?.copy(
                message = null
            )

        }

}

class RecyclerViewModelFactory(
    private val stringProvider: StringProvider,
    private val getAllPersonsUC: UsecaseGetAllPersons,
    private val getPerson: UsecaseGetPerson,
    private val deletePersonUC: UsecaseDeletePerson,
    private val addPersonUC: UsecaseAddPerson,
    private val validatedPersonUC: UsecaseValidatedPerson
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecyclerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecyclerViewModel(
                stringProvider,
                getAllPersonsUC,
                getPerson,
                deletePersonUC,
                addPersonUC,
                validatedPersonUC,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}