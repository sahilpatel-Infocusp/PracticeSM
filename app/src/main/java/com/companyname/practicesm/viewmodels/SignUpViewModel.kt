package com.companyname.practicesm.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.companyname.practicesm.auth.signUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class SignUpViewModel : ViewModel() {


    var isLoading = mutableStateOf(false)
    var errorMessage = MutableStateFlow("")

    suspend fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): Boolean {
        return try {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                signUp(name, username, email, password)
            }
            isLoading.value = false
            result
        }catch (e:Exception){
            errorMessage.value = e.toString()
            false
        }


    }

    fun resetErrorMessage() {
        errorMessage.value = ""
    }
}