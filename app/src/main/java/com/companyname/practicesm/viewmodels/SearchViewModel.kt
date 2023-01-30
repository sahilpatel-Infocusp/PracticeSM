package com.companyname.practicesm.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.companyname.practicesm.firestoreutil.getMatchedPost
import com.companyname.practicesm.models.UserPostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel:ViewModel() {
    var isLoading = mutableStateOf(false)
    val searchText = mutableStateOf("")
    var matchedPost = mutableStateOf(emptyMap<Int,UserPostModel?>())

    suspend fun getMatchedUserPost(searchText:String){
        viewModelScope.launch{
            isLoading.value = true
            getMatchedPost(searchText).collectLatest {
                matchedPost.value = it
                isLoading.value = false
            }

        }
    }

}