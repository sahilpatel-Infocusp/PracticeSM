package com.companyname.practicesm.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.companyname.practicesm.auth.getUser
import com.companyname.practicesm.auth.signUp
import com.companyname.practicesm.firestoreutil.addUserData
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
            var result1 = false
            if(result) {
                result1 = withContext(Dispatchers.IO) {
                    addUserData(getUser()!!.uid, name, username, email)
                }
            }
            isLoading.value = false
            result && result1
        }catch (e:Exception){
            errorMessage.value = e.toString()
            false
        }


    }

    fun resetErrorMessage() {
        errorMessage.value = ""
    }
}