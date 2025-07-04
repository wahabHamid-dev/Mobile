package com.wahab.myapp

import androidx.lifecycle.*
import com.wahab.myapp.data.LoginRepository
import com.wahab.myapp.data.LoggedInUser
import com.wahab.myapp.data.AppResult
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    private val repository = LoginRepository()

    private val _signupResult = MutableLiveData<AppResult<LoggedInUser>>()
    val signupResult: LiveData<AppResult<LoggedInUser>> = _signupResult

    fun signup(
        username: String,
        fullName: String,
        email: String,
        idCard: String,
        cellNumber: String,
        password: String
    ) {
        viewModelScope.launch {
            val result = repository.signup(
                username = username,
                fullName = fullName,
                email = email,
                idCard = idCard,
                cellNumber = cellNumber,
                password = password
            )
            _signupResult.postValue(result)
        }
    }
}
