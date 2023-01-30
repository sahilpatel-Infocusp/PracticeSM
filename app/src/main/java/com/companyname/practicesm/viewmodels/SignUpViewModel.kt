package com.companyname.practicesm.viewmodels

import android.util.Log
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

    var email = mutableStateOf("")
    var name = mutableStateOf("")
    var username = mutableStateOf("")
    var password = mutableStateOf("")




    suspend fun signUpUser(
        name: String,
        username: String,
        email: String,
        password: String
    ): Boolean {
        return try {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                signUp(email, password)
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
            isLoading.value = false
            errorMessage.value = e.toString()
            Log.w("SignUp",e)
            false
        }


    }

    fun resetErrorMessage() {
        errorMessage.value = ""
    }
}