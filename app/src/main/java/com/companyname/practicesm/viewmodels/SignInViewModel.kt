package com.companyname.practicesm.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.companyname.practicesm.auth.signIn
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class SignInViewModel:ViewModel() {
    var isLoading = mutableStateOf(false)
    val errorMessage = MutableStateFlow("")

    suspend fun signInUser(
        email:String,
        password:String
    ):FirebaseUser?{
        return try {
            val user = withContext(Dispatchers.IO){
                isLoading.value = true
                signIn(email,password)
            }
            isLoading.value = false
            user
        } catch (e:java.lang.Exception){
            isLoading.value = false
            Log.w("SignInUser",e)
            errorMessage.value = e.toString()
            null

        }
    }

    fun resetErrorMessage() {
        errorMessage.value = ""
    }

}