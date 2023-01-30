package com.companyname.practicesm.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.companyname.practicesm.firestoreutil.addPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class AddPostViewModel:ViewModel() {
    val errorMessage = MutableStateFlow("")

    val title = mutableStateOf("")
    val description = mutableStateOf("")

    suspend fun addUserPost(title:String,description:String):Boolean{
        return try {
            val result = withContext(Dispatchers.IO){
                    addPost(title,description)
            }
            Log.v("Result",result.toString())
            result
        }catch (e:Exception){
            Log.w("AddUserPost",e)
            errorMessage.value = e.toString()
            false
        }
    }

    fun resetErrorMessage() {
        errorMessage.value = ""
    }

}