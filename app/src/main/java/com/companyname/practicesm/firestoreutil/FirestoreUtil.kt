package com.companyname.practicesm.firestoreutil

import com.companyname.practicesm.models.UserDataModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine

private var db = Firebase.firestore

suspend fun addUserData(name:String,username:String,email:String):Boolean{
    val user = UserDataModel(name,username,email,0)
    return suspendCancellableCoroutine {
        continuation ->
        db.collection("UserData")
            .document("$username")
            .set(Gson().toJson)
    }
}