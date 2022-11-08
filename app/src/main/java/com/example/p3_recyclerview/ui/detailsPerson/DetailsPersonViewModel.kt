package com.example.p3_recyclerview.ui.detailsPerson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.p3_recyclerview.R
import com.example.p3_recyclerview.domain.model.Person
import com.example.p3_recyclerview.domain.usecases.UsecaseGetPerson
import com.example.p3_recyclerview.domain.usecases.UsecaseUpdatePerson
import com.example.p3_recyclerview.domain.usecases.UsecaseValidatedPerson
import com.example.p3_recyclerview.utils.StringProvider

class DetailsPersonViewModel (
    private val stringProvider: StringProvider,
    private val getPersonUC: UsecaseGetPerson,
    private val updatePersonUC: UsecaseUpdatePerson,
    private val validatedPersonUC: UsecaseValidatedPerson,
        ) : ViewModel(){

        private val _uiState = MutableLiveData<DetailsPersonState>()
        val uiState: LiveData<DetailsPersonState> get() = _uiState

        fun getPerson(positionPerson: Int) {
            _uiState.value = DetailsPersonState(
                person = getPersonUC.getPerson(positionPerson)
            )
        }

        fun updatePerson(pNew: Person) {
            val pLast = _uiState.value?.person
            if (pNew != pLast){
                if (pLast != null) {
                    if(validatedPersonUC.validatedPerson(pNew)){
                        updatePersonUC.updatePerson(pLast,pNew)
                        _uiState.value = DetailsPersonState(
                            person = pNew,
                            message = stringProvider.getString(R.string.niceUpdatePerson)
                        )
                    } else {
                        _uiState.value = _uiState.value?.copy(
                            //person = pLast,
                            message =  stringProvider.getString(R.string.errorUpdatePersonErrorField)
                        )
                    }
                }
            }else{
                //el copy sirve para no tener q estar reponiendo todos los datos del state otra vez, porq si no lo pones, coge los valores nulos, y con el copy coge los valores que ya tenia
                _uiState.value = _uiState.value?.copy(
                    //person = pLast,
                    message = stringProvider.getString(R.string.errorUpdatePersonChangeField)
                )
            }
        }

        fun errorMostrado() {
            //esta funcion se llama en el observe para dejar el mensaje a null de nuevo y no se quede por defecto el ultimo mensaje
            _uiState.value = _uiState.value?.copy(message = null)

        }
}

class DetailsPersonViewModelFactory(
    private val stringProvider: StringProvider,
    private val getPersonUC: UsecaseGetPerson,
    private val updatePersonUC: UsecaseUpdatePerson,
    private val validatedPersonUC: UsecaseValidatedPerson,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsPersonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsPersonViewModel(
                stringProvider,
                getPersonUC,
                updatePersonUC,
                validatedPersonUC,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}