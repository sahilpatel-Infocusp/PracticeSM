package com.companyname.practicesm.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companyname.practicesm.auth.signOut
import com.companyname.practicesm.firestoreutil.getUserData
import com.companyname.practicesm.firestoreutil.getUserPosts
import com.companyname.practicesm.models.UserDataModel
import com.companyname.practicesm.models.UserPostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.TypeVariable

class ProfilePageViewModel:ViewModel() {
    val screenLoading = mutableStateOf(false)
    val errorMessage = MutableStateFlow("")

    val postsLoading = mutableStateOf(false)

    var userData:MutableState<UserDataModel?> = mutableStateOf(null)
    var postList = mutableStateMapOf<Int,UserPostModel>()

    init {
//        viewModelScope.launch {
//            screenLoading.value = true
//            userData.value = withContext(Dispatchers.IO){ getUser() }
//            screenLoading.value = false
//        }
        viewModelScope.launch{
            screenLoading.value = true

            getUserData().collectLatest {
                userData.value = it
                screenLoading.value = false
            }

            screenLoading.value = false
        }
        viewModelScope.launch {
            postsLoading.value = true

            getUserPosts().collectLatest {
                Log.v("Here","Check")
                for(key in it.keys){
                    postList[key] = it[key]!!
                }
                postsLoading.value = false
            }
        }
    }

    // getting error in following code which uses suspendeble coroutine in firestoreutil file
    //https://stackoverflow.com/questions/66891349/java-lang-illegalstateexception-when-using-state-in-android-jetpack-compose
//    private suspend fun getUser():UserDataModel?{
//        return try {
//            screenLoading.value = true
//            val userData = withContext(Dispatchers.Main){
//                getUserData()
//            }
//            screenLoading.value = false
//            Log.v("UserData1",userData.toString())
//            userData
//        }catch (e:Exception){
//            screenLoading.value = false
//            errorMessage.value = e.toString()
//            Log.w("GetUserData",e)
//            null
//        }
//    }
    suspend fun signOutUser() {
        signOut()
    }

}