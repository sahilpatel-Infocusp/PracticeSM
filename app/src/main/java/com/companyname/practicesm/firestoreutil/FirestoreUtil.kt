package com.companyname.practicesm.firestoreutil

import android.util.Log
import com.companyname.practicesm.models.UserDataModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

private var db = Firebase.firestore

suspend fun addUserData(uid:String,name:String,username:String,email:String):Boolean{
    val user = UserDataModel(uid,name,username,email,0)
    return suspendCancellableCoroutine {
        continuation ->
        db.collection("UserData")
            .document("$username")
            .set(Gson().toJson(user))
            .addOnSuccessListener {
                Log.v("AddUser","User added")
                updateUserCount()
                continuation.resume(true, onCancellation = {})
            }
            .addOnFailureListener{
                continuation.resumeWithException(it)
            }
    }
}

fun updateUserCount(){
    db.collection("UserData")
        .document("UserCount")
        .update("UserCount",FieldValue.increment(1))
}